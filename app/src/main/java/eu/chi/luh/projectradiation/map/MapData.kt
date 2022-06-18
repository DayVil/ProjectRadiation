package eu.chi.luh.projectradiation.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import eu.chi.luh.projectradiation.RequestCodes
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
    private var countryName: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Inits with predefined values
     */
    init {
        currentPos = LatLng(52.3759, 9.7320)
        cityName = "Hannover"
        countryName = "Germany"
    }

    /**
     * Gets the LatLng of the set place
     *
     * @return Gets the LatLng
     */
    fun getPos(): LatLng {
        return currentPos
    }

    /**
     * Sets the current position for the collector.
     *
     * @param ctx Context of the application
     * @param value This is the place to be set
     */
    fun setPosition(ctx: Context, value: LatLng) {
        this.currentPos = value
        this.cityName = this.getReverseAddressName(ctx)
        this.countryName = this.getCountryFromCords(ctx)
    }

    /**
     * Sets the current position for the collector.
     *
     * @param ctx Context of the application
     * @param lat This is the lat of this place.
     * @param lon This is the lon of this place.
     */
    fun setPosition(ctx: Context, lat: Double, lon: Double) {
        this.setPosition(ctx, LatLng(lat, lon))
    }

    /**
     * Gets the Address of the given cords
     */
    private fun getReverseAddress(ctx: Context, cords: LatLng): Address? {
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

    /**
     * Gets the Address of the current pos
     */
    private fun getReverseAddress(ctx: Context): Address? {
        return getReverseAddress(ctx, this.getPos())
    }

    /**
     * Gets the Address from the given name
     */
    private fun getAddressFromName(ctx: Context, place: String): Address? {
        val nameGeolocation = Geocoder(ctx, Locale.getDefault())

        try {
            val loc = nameGeolocation.getFromLocationName(place, 1)
            if (loc != null && loc.isNotEmpty()) {
                Log.d("MapData", loc.toString())
                return loc[0]
            }
        } catch (e: IOException) {
            Log.d("MapData", "Can't get Address from name")
        }

        return null
    }

    /**
     * Gets the Location from the given place
     */
    fun getAddressLocation(ctx: Context, place: String): LatLng {
        val tmpAddress: Address? = getAddressFromName(ctx, place)
        var location = LatLng(0.0, 0.0)
        if (tmpAddress != null) {
            val lat = tmpAddress.latitude
            val lon = tmpAddress.longitude
            location = LatLng(lat, lon)
        }

        return location
    }

    /**
     * Gets the city name from given Location
     */
    fun getReverseAddressName(ctx: Context, cords: LatLng): String {
        val tmpAddress: Address? = getReverseAddress(ctx, cords)
        var ctyName = "UNKNOWN"

        if (tmpAddress != null) {
            ctyName = if (tmpAddress.locality != null) tmpAddress.locality
            else tmpAddress.adminArea
        }

        return ctyName
    }

    /**
     * Gets the city name from given Location
     */
    fun getReverseAddressName(ctx: Context): String {
        return getReverseAddressName(ctx, this.getPos())
    }

    /**
     *  Gets the country from a given Location
     */
    fun getCountryFromCords(ctx: Context, cords: LatLng): String {
        val tmpAddress: Address? = getReverseAddress(ctx, cords)
        var countryName = "UNKNOWN"
        if (tmpAddress != null) {
            countryName = tmpAddress.countryName
        }

        return countryName
    }

    /**
     * Gets the country from a given Location
     */
    fun getCountryFromCords(ctx: Context): String {
        return getCountryFromCords(ctx, this.getPos())
    }

    /**
     * Gets the city name
     */
    fun getCityName(): String {
        return cityName
    }

    fun getCountry(): String {
        return countryName
    }

    /**
     * Sets the location to the current coordinates of the device
     */
    fun setFromCurrentLocation(
        ctx: Activity,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        if (checkLocationPermissionChecker(ctx)) {
            if (isLocationEnabled(ctx)) {
                // lat lon here
                if (ActivityCompat.checkSelfPermission(
                        ctx,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        ctx,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationPermission(ctx)
                    return
                }

                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        this.setPosition(ctx, location.latitude, location.longitude)
                    }
                }

            } else {
                // open settings
                Toast.makeText(ctx, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ctx.startActivity(intent)
            }
        } else {
            // Request permission here
            requestLocationPermission(ctx)
        }
    }

    /**
     * Permission logic
     */
    private fun requestLocationPermission(ctx: Activity) {
        ActivityCompat.requestPermissions(
            ctx,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            RequestCodes.PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    /**
     * Permission logic
     */
    private fun checkLocationPermissionChecker(ctx: Activity): Boolean {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                ctx,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * Permission logic
     */
    private fun isLocationEnabled(ctx: Activity): Boolean {
        val locationManager: LocationManager =
            ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Prints the current location of the app
     */
    fun printPos(tag: String) {
        Log.d(
            tag, "My position is\n" +
                    "lat = ${this.getPos().latitude}\n" +
                    "lon = ${this.getPos().longitude}"
        )
    }
}