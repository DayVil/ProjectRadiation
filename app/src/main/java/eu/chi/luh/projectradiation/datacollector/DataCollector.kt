package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.datacollector.typecollectors.AirPollutionCollector
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

    private val _mapData = MapData.invoke()

    private var _currentTime: Long = System.currentTimeMillis()
    private var _uviData: Uvi? = null
    private var _pollenData: Pollen? = null
    private var _airData: AirPollution? = null


    private fun searchName(ls: List<Environment>, name: String): Int {
        var cnt = 0
        for (i in ls) {
            if (name == i.cityName) {
                return cnt
            }
            ++cnt
        }

        return -1
    }

    private fun getData() {
        _uviData = this._uviCollector.collect()
        _pollenData = this._pollenCollector.collect()
        _airData = this._airQualityCollector.collect()
    }

    private fun insertData() {
        val env = Environment(
            _currentTime,
            _mapData.getPos().latitude,
            _mapData.getPos().longitude,
            _mapData.getCityName(),
            _mapData.getCountry(),
            _uviData,
            _pollenData
        )

        _database.environmentDao().insertAll(env)
    }

    /**
     * Collects the data of all given types and inserts the result in the database. This can
     * only occur every hour.
     *
     * @param pause (Minutes) Decides whether the collector should collect new data between the last
     * fetch and the given time.
     */
    fun collect(pause: Long = 60) {
        _currentTime = System.currentTimeMillis()
        val pauseTime = TimeUnit.MINUTES.toMillis(pause)

        if (this._database.environmentDao().checkEmpty() == null) {
            getData()
            insertData()
        } else {
            val lessThanTime =
                (_currentTime - this._database.environmentDao().getLast().time) < pauseTime
            val ctyName = this._database.environmentDao().getLast().cityName
            val toSearchCity = _mapData.getCityName()

            if (lessThanTime) {
                if (ctyName == toSearchCity) {
                    return
                } else {
                    _database.environmentDao().getAll().let {
                        getData()
                        val idx = searchName(it, toSearchCity)
                        if (idx != -1) {
                            // TODO update Database
                        } else {
                            insertData()
                        }
                    }

                }
            }
        }
    }
}