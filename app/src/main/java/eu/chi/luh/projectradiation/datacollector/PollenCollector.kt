package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.entities.Pollen

class PollenCollector(val _apiKey: String): EnvironmentCollector<Pollen>(_apiKey) {
    override fun collect(): Pollen? {
        return Pollen()
    }
}