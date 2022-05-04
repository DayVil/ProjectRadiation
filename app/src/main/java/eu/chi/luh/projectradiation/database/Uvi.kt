package eu.chi.luh.projectradiation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Uvi(
    @PrimaryKey(autoGenerate = true) val uviId: Int? = null,
    @ColumnInfo val uviCurrent: Double,
    @ColumnInfo val uviAverage: Double,
    @ColumnInfo val uviMinimum: Double,
    @ColumnInfo val uviMaximum: Double
    )
