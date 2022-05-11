package eu.chi.luh.projectradiation.datacollector

import android.util.Log
import eu.chi.luh.projectradiation.entities.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class DataCollector(private val _database: AppDatabase, _apiOpenWeather: String, _apiAmbee: String) {
    private val _uviCollector: UviCollector = UviCollector(_apiOpenWeather)
    private val _pollenCollector: PollenCollector = PollenCollector(_apiAmbee)
    private  val _airQualityCollector: AirPollutionCollector = AirPollutionCollector(_apiAmbee)
    private val _collectors: MutableList<EnvironmentCollector<*>> = mutableListOf(_uviCollector)

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    fun collect() {
        val currentTime = System.currentTimeMillis()

        if (_database.environmentDao().checkEmpty() != null) {
            val lessOneHour =
                currentTime - _database.environmentDao().getLast().time < TimeUnit.HOURS.toMillis(1)
            if (lessOneHour)
                return
        }

        val uviData: Uvi? = this._uviCollector.collect()
        val pollenData: Pollen? = this._pollenCollector.collect()
        val airData: AirPollution? = this._airQualityCollector.collect()

        val env = Environment(currentTime, uviData)

        _database.environmentDao().insertAll(env) //TODO temporary
    }

    fun setPosition(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon

        for (collector in this._collectors) {
            collector.setPosition(lat, lon)
        }
    }
}