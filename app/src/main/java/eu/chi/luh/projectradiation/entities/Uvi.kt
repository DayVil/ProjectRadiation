package eu.chi.luh.projectradiation.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Uvi(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "response_uvi") var response: Boolean = false,
    @ColumnInfo(name = "current_uvi") var uviCurrent: Double? = null,
    @ColumnInfo(name = "average_uvi") var uviAverage: Double? = null,
    @ColumnInfo(name = "minimum_uvi") val uviMinimum: Double? = null,
    @ColumnInfo(name = "maximum_uvi") var uviMaximum: Double? = null
)
