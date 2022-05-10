package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.database.AirPollution
import eu.chi.luh.projectradiation.database.AppDatabase
import eu.chi.luh.projectradiation.database.Pollen
import eu.chi.luh.projectradiation.database.Uvi


class DataCollector(private val _database: AppDatabase, _apiOpenWeather: String, _apiAmbee: String) {
    private val _uviCollector: UviCollector = UviCollector(_apiOpenWeather)
    private val _pollenCollector: PollenCollector = PollenCollector(_apiAmbee)
    private  val _airQualityCollector: AirPollutionCollector = AirPollutionCollector(_apiAmbee)
    private val _collectors: MutableList<EnvironmentCollector<*>> = mutableListOf(_uviCollector)

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    fun collect() {
        val uviData: Uvi? = this._uviCollector.collect()
        val pollenData: Pollen? = this._pollenCollector.collect()
        val airData: AirPollution? = this._airQualityCollector.collect()

        _database.environmentDao().insertAll(uviData!!) //TODO temporary
    }

    fun setPosition(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon

        for (collector in this._collectors) {
            collector.setPosition(lat, lon)
        }
    }
}