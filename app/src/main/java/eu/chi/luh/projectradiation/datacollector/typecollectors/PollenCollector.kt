package eu.chi.luh.projectradiation.datacollector.typecollectors

import android.util.Log
import eu.chi.luh.projectradiation.entities.Pollen
import eu.chi.luh.projectradiation.entities.tmp.TemporaryData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch

/**
 * Only use the DataCollector for data collection purposes.
 */
class PollenCollector(_apiKey: String) : EnvironmentCollector<Pollen>(_apiKey) {

    override fun makeLink(): String {
        val utcTime = LocalDateTime.now(ZoneOffset.UTC)
        val nowFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val partFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T23:00:00Z'")
        val now = utcTime.format(nowFormatter)
        val part = utcTime.format(partFormatter)

        val pos = this.getPosition()
        val fields = "treeIndexMax,grassIndexMax,weedIndexMax," +
                "treeIndexMin,grassIndexMin,weedIndexMin," +
                "treeIndexAvg,grassIndexAvg,weedIndexAvg"

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
        // TODO finish

        return Pollen(
            response = true,
            pollenCurrent = 0,
            pollenAverage = 0,
            pollenMinimum = 0,
            pollenMaximum = 0,
            pollenGrassCurrent = 0,
            pollenGrassAverage = 0,
            pollenGrassMinimum = 0,
            pollenGrassMaximum = 0,
            pollenTreeCurrent = 0,
            pollenTreeAverage = 0,
            pollenTreeMinimum = 0,
            pollenTreeMaximum = 0
        )
    }


    override fun collect(): Pollen? {
        val rUrl = makeLink()
        this.request = Request.Builder()
            .url(rUrl)
            .get()
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Encoding", "gzip")
            .build()

        val countDownLatch = CountDownLatch(1)
        var pollenData: Pollen? = null
        if (TemporaryData.checkPos(getPosition())) {
            throw AssertionError("Positions are not the same.")
        }

        this.client.newCall(this.request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("POLLEN", "Successfull connection")
                val rBody = response.body?.string()
                pollenData = getPollenData(rBody)

                countDownLatch.countDown()
            }

        })

        return pollenData
    }
}