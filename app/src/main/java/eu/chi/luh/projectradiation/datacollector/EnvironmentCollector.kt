package eu.chi.luh.projectradiation.datacollector

abstract class EnvironmentCollector(private val _apiKey: String) {
    private var _lat: Double = 0.0
    private var _lon: Double = 0.0

    fun setPosition(lat: Double, lon: Double) {
        this._lat = lat
        this._lon = lon
    }

    abstract fun collect()
}