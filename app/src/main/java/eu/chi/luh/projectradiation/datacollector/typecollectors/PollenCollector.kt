package eu.chi.luh.projectradiation.datacollector.typecollectors

import android.util.Log
import eu.chi.luh.projectradiation.entities.Pollen
import eu.chi.luh.projectradiation.entities.tmp.TemporaryData
import eu.chi.luh.projectradiation.mathfunction.CompareOp
import eu.chi.luh.projectradiation.mathfunction.greaterLesserFun
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch

/**
 * Only use the DataCollector for data collection purposes.
 */
class PollenCollector(_apiKey: String) : EnvironmentCollector<Pollen>(_apiKey) {

    private fun getAverage(data: JSONArray, searchKeyword: String): Double {
        var sum = 0.0

        val amountVal = data.length()
        for (hourlyIndex in 0 until amountVal) {
            val dataSet: JSONObject = data.getJSONObject(hourlyIndex)
            val hourlyData = dataSet.getJSONObject("values").getInt(searchKeyword)
            sum += hourlyData
        }

        return sum / amountVal.toDouble()
    }

    private fun getExtreme(
        data: JSONArray,
        searchKeyword: String,
        compareVal: Int,
        operator: CompareOp
    ): Int {
        val tmpFun: (Int, Int) -> Boolean = greaterLesserFun(operator)

        var extremeAmount = compareVal

        for (hourlyIndex in 0 until data.length()) {
            val dataSet: JSONObject = data.getJSONObject(hourlyIndex)
            val hourlyData = dataSet.getJSONObject("values").getInt(searchKeyword)

            if (tmpFun(hourlyData, extremeAmount)) extremeAmount = hourlyData
        }
        return extremeAmount
    }

    override fun makeLink(): String {
        val utcTime = LocalDateTime.now(ZoneOffset.UTC)
        //TODO between 23:00-23:59 utc this will crash
        val nowFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00:00Z'")
        val partFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T23:00:00Z'")
        val now = utcTime.format(nowFormatter)
        val part = utcTime.format(partFormatter)

        val pos = this.getPosition()
        val fields = "treeIndex,grassIndex,weedIndex"

        val rUrl = "https://api.tomorrow.io/v4/timelines?" +
                "location=${pos.latitude},${pos.longitude}&" +
                "timesteps=1h&" +
                "startTime=$now&" +
                "endTime=$part&" +
                "units=metric&" +
                "apikey=${this._apiKey}&" +
                "fields=$fields"

        Log.d("POLLEN", rUrl)
        return rUrl
    }

    private fun getPollenData(rawStringJson: String?): Pollen {
        if (rawStringJson == null) throw NullPointerException("The request body is null!")
        val rawData = JSONObject(rawStringJson)

        val intervals = rawData.getJSONObject("data").getJSONArray("timelines").getJSONObject(0)
            .getJSONArray("intervals")

        val currentData = intervals.getJSONObject(0).getJSONObject("values")

        //TODO rewrite as loop
        val pollenGrassCurrent = currentData.getInt("grassIndex")
        val pollenGrassAverage = this.getAverage(intervals, "grassIndex")
        val pollenGrassMinimum =
            this.getExtreme(intervals, "grassIndex", pollenGrassCurrent, CompareOp.LESSER)
        val pollenGrassMaximum =
            this.getExtreme(intervals, "grassIndex", pollenGrassCurrent, CompareOp.GREATER)

        val pollenTreeCurrent = currentData.getInt("treeIndex")
        val pollenTreeAverage = this.getAverage(intervals, "treeIndex")
        val pollenTreeMinimum =
            this.getExtreme(intervals, "treeIndex", pollenGrassCurrent, CompareOp.LESSER)
        val pollenTreeMaximum =
            this.getExtreme(intervals, "treeIndex", pollenGrassCurrent, CompareOp.GREATER)

        val pollenWeedCurrent = currentData.getInt("weedIndex")
        val pollenWeedAverage = this.getAverage(intervals, "weedIndex")
        val pollenWeedMinimum =
            this.getExtreme(intervals, "weedIndex", pollenGrassCurrent, CompareOp.LESSER)
        val pollenWeedMaximum =
            this.getExtreme(intervals, "weedIndex", pollenGrassCurrent, CompareOp.GREATER)

        val pollenCurrent = (pollenGrassCurrent + pollenTreeCurrent + pollenWeedCurrent) / 3.0
        val pollenAverage: Double =
            (pollenGrassAverage + pollenTreeAverage + pollenWeedAverage) / 3.0
        val pollenMinimum = (pollenGrassMinimum + pollenTreeMinimum + pollenWeedMinimum) / 3.0
        val pollenMaximum = (pollenGrassMaximum + pollenTreeMaximum + pollenWeedMaximum) / 3.0

        return Pollen(
            response = true,

            pollenCurrent = pollenCurrent,
            pollenAverage = pollenAverage,
            pollenMinimum = pollenMinimum,
            pollenMaximum = pollenMaximum,

            pollenGrassCurrent = pollenGrassCurrent,
            pollenGrassAverage = pollenGrassAverage,
            pollenGrassMinimum = pollenGrassMinimum,
            pollenGrassMaximum = pollenGrassMaximum,

            pollenTreeCurrent = pollenTreeCurrent,
            pollenTreeAverage = pollenTreeAverage,
            pollenTreeMinimum = pollenTreeMinimum,
            pollenTreeMaximum = pollenTreeMaximum,

            pollenWeedCurrent = pollenWeedCurrent,
            pollenWeedAverage = pollenWeedAverage,
            pollenWeedMinimum = pollenWeedMinimum,
            pollenWeedMaximum = pollenWeedMaximum
        )
    }


    override fun collect(): Pollen? {
        val rUrl = makeLink()
        this.request = Request.Builder()
            .url(rUrl)
            .get()
            .build()

        val countDownLatch = CountDownLatch(1)
        var pollenData: Pollen? = null
        if (TemporaryData.checkPos(getPosition())) {
            throw AssertionError("Positions are not the same.")
        }

        this.client.newCall(this.request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Pollen", "Error on response.")
                pollenData = Pollen(false)

                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("POLLEN", "Successfull connection")
                val rBody = response.body?.string()
                pollenData = getPollenData(rBody)

                countDownLatch.countDown()
            }

        })

        countDownLatch.await()
        return pollenData
    }
}