package eu.chi.luh.projectradiation.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AirPollution(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "response_airquality") var response: Boolean = false,

    @ColumnInfo(name = "current_airquality") var aqi: Double? = null,
    @ColumnInfo(name = "average_airquality") var aqiAverage: Double? = null,
    @ColumnInfo(name = "maximum_airquality") var aqiMaximum: Double? = null,
    @ColumnInfo(name = "minimum_airquality") var aqiMinimum: Double? = null,
)