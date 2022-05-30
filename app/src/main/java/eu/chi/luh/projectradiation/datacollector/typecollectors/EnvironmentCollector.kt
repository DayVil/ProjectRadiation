package eu.chi.luh.projectradiation.datacollector.typecollectors

import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * This is the interface for the collectors of a specific api.
 */
abstract class EnvironmentCollector<T>(protected val _apiKey: String) {
    /** Creates a client to request data. */
    protected val client: OkHttpClient = OkHttpClient()

    /** The url to send to the api. */
    protected lateinit var request: Request

    private var _lat: Double = 0.0
    private var _lon: Double = 0.0

    /**
     * Sets the position of this collector.
     *
     * @param lat latitude of this position
     * @param lon longitude of this position
     */
    fun setPosition(lat: Double, lon: Double) {
        this._lat = lat
        this._lon = lon
    }

    /**
     * Sets the position of this collector.
     *
     * @param pos Position of this collector.
     */
    fun setPosition(pos: LatLng) {
        this.setPosition(pos.latitude, pos.longitude)
    }

    protected fun getPosition(): LatLng {
        return LatLng(this._lat, this._lon)
    }

    /**
     * Creates a usable Link to request to.
     *
     * @return returns the link as string
     */
    protected abstract fun makeLink(): String

    /**
     * Collects the data from the api and makes it workable.
     *
     * @return Returns a data class of a generic type.
     */
    abstract fun collect(): T?
}