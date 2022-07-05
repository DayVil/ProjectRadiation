package eu.chi.luh.projectradiation.datacollector

import android.content.Context
import eu.chi.luh.projectradiation.datacollector.typecollectors.AirPollutionCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.PollenCollector
import eu.chi.luh.projectradiation.datacollector.typecollectors.UviCollector
import eu.chi.luh.projectradiation.entities.*
import eu.chi.luh.projectradiation.map.MapData

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
        AirPollutionCollector(this._apiOpenWeather)

    private val _mapData = MapData.invoke()

    private var _currentTime: Long = System.currentTimeMillis()
    private var _uviData: Uvi? = null
    private var _pollenData: Pollen? = null
    private var _airData: AirPollution? = null


    private fun searchName(ls: List<Environment>, name: String): Environment? {
        for ((cnt, i) in ls.withIndex()) {
            if (name == i.cityName) {
                return ls[cnt]
            }
        }

        return null
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
            _pollenData,
            _airData
        )

        _database.environmentDao().insertAll(env)
    }

    /**
     * Collects the data of all given types and inserts the result in the database. This can
     * only occur every hour.
     *
     * fetch and the given time.
     */
    fun collect() {
        _currentTime = System.currentTimeMillis()
        val databaseEntries = _database.environmentDao().getAll()

        if (databaseEntries.isEmpty()) {
            getData()
            insertData()
        } else {
            val toSearchCity = _mapData.getCityName()

            val env = searchName(databaseEntries, toSearchCity)
            if (env != null) {
                _database.environmentDao().delete(env)
                getData()
                insertData()
            } else {
                getData()
                insertData()
            }

        }
    }

    fun collectAll(ctx: Context) {
        val databaseEntries = _database.environmentDao().getAll().reversed()
        val currentPos = _mapData.getPos()

        for (i in databaseEntries) {
            _mapData.setPosition(ctx, i.lat, i.lon)
            this.collect()
        }

        _mapData.setPosition(ctx, currentPos)
    }
}