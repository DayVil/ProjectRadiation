package eu.chi.luh.projectradiation.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

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

    private var currentPos: LatLng
    private var cityName: String

    init {
        currentPos = LatLng(52.3759, 9.7320)
        cityName = "Hannover"
    }

    fun getPos(): LatLng {
        return currentPos
    }

    fun setPosition(ctx: Context, value: LatLng) {
        this.currentPos = value
        val nowAddress = this.getReverseAddress(ctx)
        if (nowAddress != null) {
            this.cityName = nowAddress.locality
        } else this.cityName = "UNKNOWN"
    }

    fun setPosition(ctx: Context, lat: Double, lon: Double) {
        this.setPosition(ctx, LatLng(lat, lon))
    }

    fun printPos(tag: String) {
        Log.d(
            tag, "My position is\n" +
                    "lat = ${this.getPos().latitude}\n" +
                    "lon = ${this.getPos().longitude}"
        )
    }

    fun getReverseAddress(ctx: Context): Address? {
        val reverseGeolocation = Geocoder(ctx, Locale.getDefault())

        try {
            val addressNow = reverseGeolocation.getFromLocation(
                this.currentPos.latitude,
                this.currentPos.longitude,
                1
            )
            if (addressNow != null && addressNow.isNotEmpty()) {
                Log.d("MapData", addressNow.toString())
                return addressNow[0]
            }

        } catch (e: IOException) {
            Toast.makeText(ctx, "Unable connect to Geocoder", Toast.LENGTH_LONG).show()
        }

        return null
    }

    fun getCityName(): String {
        return cityName
    }
}