package eu.chi.luh.projectradiation.datacollector.typecollectors

import eu.chi.luh.projectradiation.entities.Pollen
import okhttp3.Request
import java.util.concurrent.CountDownLatch

/**
 * Only use the DataCollector for data collection purposes.
 */
class PollenCollector(val _apiKey: String): EnvironmentCollector<Pollen>(_apiKey) {

    init { //TODO: Lat and Lon to city converter.
    }

    override fun collect(): Pollen? {
        val countDownLatch = CountDownLatch(1)
        return Pollen()
    }
}