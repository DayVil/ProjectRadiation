package eu.chi.luh.projectradiation.datacollector.typecollectors

import android.util.Log
import eu.chi.luh.projectradiation.entities.Uvi
import eu.chi.luh.projectradiation.entities.tmp.TemporaryData
import eu.chi.luh.projectradiation.mathfunction.CompareOp
import eu.chi.luh.projectradiation.mathfunction.getAverage
import eu.chi.luh.projectradiation.mathfunction.getExtreme
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
class UviCollector(_apiKey: String): EnvironmentCollector<Uvi>(_apiKey) {
    private val _exclude = "daily,minutely"

    /**
     * Calculates the [current, minimum, maximum, average] uvi values over a day.
     *
     * @param rawStringJson The raw data string where then information will be extracted from
     * @return Returns [current, minimum, maximum, average] uvi values
     */
    private fun getUviData(rawStringJson: String?): Uvi {
        if (rawStringJson == null) throw NullPointerException("The request body is null!")
        val rawData = JSONObject(rawStringJson)

        val hourlySet: JSONArray = rawData.getJSONArray("hourly")

        val current: Double = rawData.getJSONObject("current").getDouble("uvi")

        val minVal: Double = getExtreme(hourlySet, "uvi", current, CompareOp.LESSER)
        val maxVal: Double = getExtreme(hourlySet, "uvi", current, CompareOp.GREATER)

        val average: Double = getAverage(hourlySet, "uvi")

        return Uvi(
            response = true, uviCurrent = current, uviAverage = average,
            uviMinimum = minVal, uviMaximum = maxVal
        )
    }

    override fun makeLink(): String {
        val pos = getPosition()
        val rUrl = "https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=${pos.latitude}&lon=${pos.longitude}&exclude=${this._exclude}&appid=${this._apiKey}"
        Log.d("UVI", rUrl)
        return rUrl
    }

    /**
     * Runs the process of fetching uvi data of a day and processing it.
     */
    override fun collect(): Uvi? {
        val rUrl = makeLink()
        this.request = Request.Builder().url(rUrl).build()

        val countDownLatch = CountDownLatch(1)
        var uviData: Uvi? = null
        if (TemporaryData.checkPos(getPosition())) {
            throw AssertionError("Positions are not the same.")
        }

        this.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                Log.d("UVI", "Error on response.")
                uviData = Uvi(false)
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("UVI", "Successful connection")
                val rBody = response.body?.string()
                uviData = getUviData(rBody)

                Log.d(
                    "UVI",
                    "uvi measures: minimum=${uviData?.uviMinimum}\tmaximum=${uviData?.uviMaximum}" +
                            "\taverage=${uviData?.uviAverage}\tcurrent=${uviData?.uviCurrent}"
                )
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return uviData
    }
}
