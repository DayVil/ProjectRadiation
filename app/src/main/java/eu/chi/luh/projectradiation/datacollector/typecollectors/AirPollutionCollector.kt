package eu.chi.luh.projectradiation.datacollector.typecollectors

import eu.chi.luh.projectradiation.entities.AirPollution

/**
 * Only use the DataCollector for data collection purposes.
 */
class AirPollutionCollector(private val _apiKey: String): EnvironmentCollector<AirPollution>(_apiKey) {
    override fun collect(): AirPollution? {
        return AirPollution()
    }
}