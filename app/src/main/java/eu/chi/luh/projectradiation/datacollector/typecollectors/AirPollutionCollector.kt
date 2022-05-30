package eu.chi.luh.projectradiation.datacollector.typecollectors

import eu.chi.luh.projectradiation.entities.AirPollution

/**
 * Only use the DataCollector for data collection purposes.
 */
class AirPollutionCollector(_apiKey: String) : EnvironmentCollector<AirPollution>(_apiKey) {
    override fun makeLink(): String {
        TODO("Not yet implemented")
    }

    override fun collect(): AirPollution? {
        return AirPollution()
    }
}