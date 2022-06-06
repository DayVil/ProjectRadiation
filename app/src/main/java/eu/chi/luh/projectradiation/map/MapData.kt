package eu.chi.luh.projectradiation.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient

/**
 * These are some global variables to use.
 * WARNING: only one currentPos should exist and db.
 */
class MapData(var mapsApi: String?) {
    companion object {
        @Volatile
        private var instance: MapData? = null
        private val LOCK = Any()

        operator fun invoke(key: String? = null) = instance ?: synchronized(LOCK) {
            instance ?: createMapData(key).also { instance = it }
        }

        private fun createMapData(key: String?) = MapData(key)
    }

    private var currentPos: LatLng = LatLng(52.512454, 13.416506)
    private var client: OkHttpClient = OkHttpClient()

    fun getPos(): LatLng {
        return currentPos
    }

    fun setPosition(value: LatLng) {
        currentPos = value
    }

    fun setPosition(lat: Double, lon: Double) {
        currentPos = LatLng(lat, lon)
    }

    fun printPos(tag: String) {
        Log.d(
            tag, "My position is\n" +
                    "lat = ${this.getPos().latitude}\n" +
                    "lon = ${this.getPos().longitude}"
        )
    }

//    fun getFullLocation(): JSONObject? {
//        val countDownLatch = CountDownLatch(1)
//
//        var retJSON: JSONObject? = null
//        val link = "https://maps.googleapis.com/maps/api/geocode/json?" +
//                "latlng=${currentPos.latitude},${currentPos.longitude}&" +
//                "key=${mapsApi}"
//
//        val request = Request.Builder().url(link).build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                retJSON = null
//                countDownLatch.countDown()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                Log.d("UVI", "Successful connection")
//                val rawSJSON = response.body?.string() ?: return onFailure(
//                    call,
//                    IOException("EMPTY")
//                )
//                retJSON = JSONObject(rawSJSON)
//                countDownLatch.countDown()
//            }
//
//        })
//
//        countDownLatch.await()
//        Log.d("MapData", retJSON.toString())
//        return retJSON
//    }
}