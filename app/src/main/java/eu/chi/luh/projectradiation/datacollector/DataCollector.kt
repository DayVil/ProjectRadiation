package eu.chi.luh.projectradiation.datacollector

import com.google.android.gms.maps.model.LatLng
import eu.chi.luh.projectradiation.datacollector.typecollectors.AirPollutionCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.EnvironmentCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.PollenCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.UviCollector
import eu.chi.luh.projectradiation.entities.*
import eu.chi.luh.projectradiation.entities.tmp.TemporaryData.Companion.currentPos
import java.util.concurrent.TimeUnit

/**
 * Collects data and stores it inside the database.
 */
class DataCollector(
    private val _database: AppDatabase,
    private val _apiOpenWeather: String,
    private val _tomorrowAPI: String
) {
    private val _uviCollector: UviCollector = UviCollector(this._apiOpenWeather)
    private val _pollenCollector: PollenCollector = PollenCollector(this._tomorrowAPI)
    private val _airQualityCollector: AirPollutionCollector =
        AirPollutionCollector(this._tomorrowAPI)
    private val _collectors: MutableList<EnvironmentCollector<*>> =
        mutableListOf(this._uviCollector, this._pollenCollector, this._airQualityCollector)

    private var _lat: Double = 0.0
    private var _lon: Double = 0.0

    init {
        this.setPosition(currentPos.latitude, currentPos.longitude)
    }

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

    /**
     * Sets the position of everything to this given position
     */
    fun setPosition(lat: Double, lon: Double) {
        this._lat = lat
        this._lon = lon
        currentPos = LatLng(this._lat, this._lon)

        for (collector in this._collectors) {
            collector.setPosition(this._lat, this._lon)
        }
    }

    /**
     * Sets the position of everything to this given position
     */
    fun setPosition(pos: LatLng) {
        this.setPosition(pos.latitude, pos.longitude)
    }
}