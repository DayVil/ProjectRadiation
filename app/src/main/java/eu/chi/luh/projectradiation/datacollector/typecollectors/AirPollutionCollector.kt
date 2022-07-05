package eu.chi.luh.projectradiation.datacollector.typecollectors

import android.util.Log
import eu.chi.luh.projectradiation.entities.AirPollution
import eu.chi.luh.projectradiation.map.MapData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch

/**
 * Only use the DataCollector for data collection purposes.
 */
class AirPollutionCollector(_apiKey: String) : EnvironmentCollector<AirPollution>(_apiKey) {
    private val mapData = MapData.invoke()

    /**
     * Calculates the [current, minimum, maximum, average] uvi values over a day.
     *
     * @param rawStringJson The raw data string where then information will be extracted from
     * @return Returns [current, minimum, maximum, average] uvi values
     */
    private fun getAirData(rawStringJson: String?): AirPollution {
        if (rawStringJson == null) throw NullPointerException("The request body is null!")
        val rawData = JSONObject(rawStringJson)

        val hourlySet: JSONArray = rawData.getJSONArray("list")

        val current: Double = hourlySet.getJSONObject(0).getJSONObject("main").getDouble("aqi")

        val lenSet = hourlySet.length()
        var sum = 0.0
        var minVal: Double = current
        var maxVal: Double = current
        for (hourlyIndex in 0 until lenSet) {
            val tmp = hourlySet.getJSONObject(hourlyIndex).getJSONObject("main").getDouble("aqi")
            if (minVal > tmp) minVal = tmp
            if (maxVal < tmp) maxVal = tmp
            sum += tmp
        }

        val average: Double = sum / lenSet

        return AirPollution(
            response = true,

            aqi = current,
            aqiAverage = average,
            aqiMaximum = maxVal,
            aqiMinimum = minVal
        )
    }

    override fun makeLink(): String {
        val pos = mapData.getPos()
        val rUrl = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?" +
                "lat=${pos.latitude}&lon=${pos.longitude}&appid=${this._apiKey}"
        Log.d("AIR", rUrl)
        return rUrl
    }

    /**
     * Runs the process of fetching uvi data of a day and processing it.
     */
    override fun collect(): AirPollution? {
        val countDownLatch = CountDownLatch(1)
        val rUrl = makeLink()
        this.request = Request.Builder().url(rUrl).build()

        var airData: AirPollution? = null

        this.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("AIR", "Error on response.")
                airData = AirPollution(false)
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("AIR", "Successful connection")
                val rBody = response.body?.string()
                airData = try {
                    getAirData(rBody)
                } catch (_: Exception) {
                    AirPollution(false)
                }
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return airData
    }
}