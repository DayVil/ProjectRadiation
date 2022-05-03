package eu.chi.luh.projectradiation.datacollector

import android.util.Log
import eu.chi.luh.projectradiation.database.Uvi
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch

class SolarRadiation(solarApiKey: String, lat: Double, lon: Double) {

    private val client: OkHttpClient
    private val request: Request

    private val exclude = "daily,minutely"

    init {
        val rUrl = "https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=$lat&lon=$lon&exclude=$exclude&appid=$solarApiKey"
        client = OkHttpClient()
        request = Request.Builder().url(rUrl).build()
    }

    /**
     * Finds the extreme values of a given JSONArray with a given key and comparator ('<' or '>')
     *
     * @exception Exception The exception will be thrown if the defined comparator is not within
     *                      the functions limits.
     * @param data The JSON Array to be searched.
     * @param searchKeyword The key for the json dictionary
     * @param compareValue This is the comparison point which the other values will be compared to
     * @param define defines the comparator ('<' or '>')
     * @return Return the extreme value
     */
    private fun getExtreme(data: JSONArray, searchKeyword: String, compareValue: Double, define: Char): Double {
        val tmpFun: (Double, Double) -> Boolean = when (define) {
            '<' -> { a: Double, b: Double -> a < b }
            '>' -> { a: Double, b: Double -> a > b }
            else -> throw Exception("Only '<' or '>' are allowed")
        }
        var extremeAmount: Double = compareValue

        for (hourlyIndex in 0 until data.length()) {
            val dataSet: JSONObject = data.getJSONObject(hourlyIndex)
            val hourlyUvi = dataSet.getDouble(searchKeyword)

            if (tmpFun(hourlyUvi, extremeAmount)) extremeAmount = hourlyUvi
        }

        return extremeAmount
    }

    /**
     * Calculates the [current, minimum, maximum, average] uvi values over a day.
     *
     * @param rawData The raw data where then information will be extracted from
     * @return Returns [current, minimum, maximum, average] uvi values
     */
    private fun getUviData(rawData: JSONObject): Uvi {
        val hourlySet: JSONArray = rawData.getJSONArray("hourly")

        val current: Double = rawData.getJSONObject("current").getDouble("uvi")
        val minVal: Double = getExtreme(hourlySet, "uvi", current, '<')
        val maxVal: Double = getExtreme(hourlySet, "uvi", current, '>')

        var sum = 0.0
        for (hourlyIndex in 0 until hourlySet.length()) {
            val dataSet: JSONObject = hourlySet.getJSONObject(hourlyIndex)
            val hourlyUvi: Double = dataSet.getDouble("uvi")
            sum += hourlyUvi
        }
        val average: Double = sum / hourlySet.length()

        return Uvi(current, average, minVal, maxVal)
    }


    /**
     * Parses and calculates the necessary data and returns it as a Uvi
     * @param rawStringJson The raw string of the responded request
     * @return returns uvi data as Uvi
     */
    private fun requestDataManipulation(rawStringJson: String?): Uvi {
        if (rawStringJson == null) throw NullPointerException("The request body is null!")
        val jsonResponse = JSONObject(rawStringJson)
        return getUviData(jsonResponse)
    }

    /**
     * Runs the process of fetching uvi data of a day and processing it.
     */
    fun run(): Uvi? {
        val countDownLatch = CountDownLatch(1)
        var uviData: Uvi? = null
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                Log.d("OPEN WEATHER", "Error on response.")
                throw IOException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("OPEN WEATHER", "Successfull connection")
                val rBody = response.body?.string()
                uviData = requestDataManipulation(rBody)

                Log.d(
                    "OPEN WEATHER",
                    "uvi measures: minimum=${uviData?.minimum}\tmaximum=${uviData?.maximum}" +
                            "\taverage=${uviData?.average}\tcurrent=${uviData?.current}"
                )
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return uviData
    }
}
