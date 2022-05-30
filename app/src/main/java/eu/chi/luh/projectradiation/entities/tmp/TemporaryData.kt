package eu.chi.luh.projectradiation.entities.tmp

import com.google.android.gms.maps.model.LatLng
import eu.chi.luh.projectradiation.entities.AppDatabase

/**
 * These are some global variables to use.
 * WARNING: only one currentPos should exist and db.
 */
class TemporaryData {
    companion object {
        /** The global current position of this application.
         * Warning only write in this via Datacollector current pos
         */
        var currentPos: LatLng = LatLng(52.512454, 13.416506)

        /** The global database of this app. */
        lateinit var db: AppDatabase

        /**
         * Checks if the positions are the same.
         *
         * @param givenPos the position to check.
         * @return True if same. False if differs.
         */
        fun checkPos(givenPos: LatLng): Boolean {
            return givenPos != this.currentPos
        }
    }
}