package eu.chi.luh.projectradiation.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    init {
        currentPos = LatLng(52.3759, 9.7320)
        cityName = "Hannover"
    }

    fun getPos(): LatLng {
        return currentPos
    }

    fun setPosition(ctx: Context, value: LatLng) {
        this.currentPos = value
        this.cityName = this.getReverseAddressName(ctx)
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

    fun getReverseAddress(ctx: Context, cords: LatLng): Address? {
        val reverseGeolocation = Geocoder(ctx, Locale.getDefault())

        try {
            val addressNow = reverseGeolocation.getFromLocation(
                cords.latitude,
                cords.longitude,
                1
            )
            if (addressNow != null && addressNow.isNotEmpty()) {
                Log.d("MapData", addressNow.toString())
                return addressNow[0]
            }

        } catch (e: IOException) {
            Log.d("MapData", "Can't get getReverseAddress")
        }

        return null
    }

    fun getReverseAddress(ctx: Context): Address? {
        return getReverseAddress(ctx, this.getPos())
    }

    fun getReverseAddressName(ctx: Context, cords: LatLng): String {
        val tmpAddress: Address? = getReverseAddress(ctx, cords)
        var ctyName = "UNKNOWN"
        if (tmpAddress != null) {
            ctyName = tmpAddress.locality
        }

        return ctyName
    }

    fun getReverseAddressName(ctx: Context): String {
        return getReverseAddressName(ctx, this.getPos())
    }

    fun getCityName(): String {
        return cityName
    }
}

// TODO make this work

//    fun getCurrentLocation(ctx: Activity, fusedLocationProviderClient: FusedLocationProviderClient): LatLng {
//        if (checkLocationPermissionChecker(ctx)) {
//            if (isLocationEnabled(ctx)) {
//                // lat lon here
//                if (ActivityCompat.checkSelfPermission(
//                        ctx,
//                        android.Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        ctx,
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    requestLocationPermission(ctx)
//                }
//
//                // TODO keine Ahnung wie man einen Wrt zurück gibt
//                val def = CompletableDeferred<Location?>()
//                val task = fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
//                    def.complete(location)
//                }
//                val loc = def.await() ?: return LatLng(0.0, 0.0)
//
//                return LatLng(loc.latitude, loc.longitude)
//
//            } else {
//                // open settings
//                Toast.makeText(ctx, "Turn on location", Toast.LENGTH_SHORT).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                ctx.startActivity(intent)
//            }
//        } else {
//            // Request permission here
//            requestLocationPermission(ctx)
//        }
//
//        return LatLng(0.0, 0.0)
//    }
//
//    private fun requestLocationPermission(ctx: Activity) {
//        ActivityCompat.requestPermissions(ctx,
//            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
//            RequestCodes.PERMISSION_REQUEST_ACCESS_LOCATION
//        )
//    }
//
//    private fun checkLocationPermissionChecker(ctx: Activity): Boolean {
//        if (ActivityCompat.checkSelfPermission(ctx,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                &&
//                ActivityCompat.checkSelfPermission(ctx,
//                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return true
//        }
//        return false
//    }
//
//    private fun isLocationEnabled(ctx: Activity): Boolean {
//        val locationManager: LocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//}