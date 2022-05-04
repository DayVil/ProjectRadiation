package eu.chi.luh.projectradiation.datacollector

import eu.chi.luh.projectradiation.database.Pollen

class DataCollector {
    private lateinit var _uviCollector: UviCollector
    private lateinit var _pollenData: Pollen

    init {
        this.collect()
    }

    fun collect() {

    }
}