package eu.chi.luh.projectradiation.entities.tmp

import com.google.android.gms.maps.model.LatLng
import eu.chi.luh.projectradiation.entities.AppDatabase

class TemporaryData {
    companion object {
        var currentPos: LatLng = LatLng(52.512454, 13.416506)
        lateinit var db: AppDatabase
    }
}