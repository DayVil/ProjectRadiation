package eu.chi.luh.projectradiation.datacollector.typecollectors

import eu.chi.luh.projectradiation.entities.Pollen

/**
 * Only use the DataCollector for data collection purposes.
 */
class PollenCollector(val _apiKey: String): EnvironmentCollector<Pollen>(_apiKey) {
    override fun collect(): Pollen? {
        return Pollen()
    }
}