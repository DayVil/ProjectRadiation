package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.entities.AirPollution

class AirPollutionCollector(private val _apiKey: String): EnvironmentCollector<AirPollution>(_apiKey) {
    override fun collect(): AirPollution? {
        return AirPollution()
    }
}