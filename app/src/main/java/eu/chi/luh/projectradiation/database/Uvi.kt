package eu.chi.luh.projectradiation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Uvi(
    @PrimaryKey(autoGenerate = true) val uviId: Int = 0,
    @ColumnInfo(name = "current_uvi") val uviCurrent: Double,
    @ColumnInfo(name = "average_uvi") val uviAverage: Double,
    @ColumnInfo(name = "minimum_uvi") val uviMinimum: Double,
    @ColumnInfo(name = "maximum_uvi") val uviMaximum: Double
)
