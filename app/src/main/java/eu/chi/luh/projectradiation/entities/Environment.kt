package eu.chi.luh.projectradiation.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Environment(
    @PrimaryKey val time: Long,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lon: Double,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "country_name") val countryName: String,
    @Embedded val uvi: Uvi?,
    @Embedded val pollen: Pollen?
//    @Embedded val airPollution: AirPollution?
)
