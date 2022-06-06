package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.datacollector.typecollectors.AirPollutionCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.EnvironmentCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.PollenCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.UviCollector
import eu.chi.luh.projectradiation.entities.*
import eu.chi.luh.projectradiation.map.MapData
import java.util.concurrent.TimeUnit

/**
 * Collects data and stores it inside the database.
 */
class DataCollector(
    private val _database: ProjectRadiationDatabase,
    private val _apiOpenWeather: String,
    private val _tomorrowAPI: String
) {
    private val _uviCollector: UviCollector = UviCollector(this._apiOpenWeather)
    private val _pollenCollector: PollenCollector = PollenCollector(this._tomorrowAPI)
    private val _airQualityCollector: AirPollutionCollector =
        AirPollutionCollector(this._tomorrowAPI)
    private val _collectors: MutableList<EnvironmentCollector<*>> =
        mutableListOf(this._uviCollector, this._pollenCollector, this._airQualityCollector)

    private val mapData = MapData.invoke()

    /**
     * Collects the data of all given types and inserts the result in the database. This can
     * only occur every hour.
     *
     * @param pause (Minutes) Decides whether the collector should collect new data between the last
     * fetch and the given time.
     */
    fun collect(pause: Long = 60) {
        val currentTime = System.currentTimeMillis()
        val pauseTime = TimeUnit.MINUTES.toMillis(pause)

        if (this._database.environmentDao().checkEmpty() != null) {
            val lessOneHour =
                (currentTime - this._database.environmentDao().getLast().time) < pauseTime
            if (lessOneHour)
                return
        }

        val uviData: Uvi? = this._uviCollector.collect()
        val pollenData: Pollen? = this._pollenCollector.collect()
        val airData: AirPollution? = this._airQualityCollector.collect()

        val env = Environment(currentTime, uviData, pollenData)

        this._database.environmentDao().insertAll(env)
    }
}