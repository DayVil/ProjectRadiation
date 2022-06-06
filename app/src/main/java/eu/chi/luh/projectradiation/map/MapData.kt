package eu.chi.luh.projectradiation.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng

/**
 * These are some global variables to use.
 * WARNING: only one currentPos should exist and db.
 */
class MapData {
    companion object {
        @Volatile
        private var instance: MapData? = null
        private val LOCK = Any()

        operator fun invoke() = instance ?: synchronized(LOCK) {
            instance ?: createMapData().also { instance = it }
        }

        private fun createMapData() = MapData()
    }

    private var currentPos: LatLng = LatLng(52.512454, 13.416506)

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
}