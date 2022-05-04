package eu.chi.luh.projectradiation.datacollector

import okhttp3.OkHttpClient
import okhttp3.Request

abstract class EnvironmentCollector<T>(private val _apiKey: String) {
    protected val client: OkHttpClient = OkHttpClient()

    protected lateinit var request: Request

    protected var lat: Double = 0.0
    protected var lon: Double = 0.0

    fun setPosition(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
    }

    abstract fun collect(): T?
}