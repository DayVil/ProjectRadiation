package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.database.AirPollution

class AirPollutionCollector(private val _apiKey: String): EnvironmentCollector<AirPollution>(_apiKey) {
    override fun collect(): AirPollution? {
        return AirPollution()
    }
}