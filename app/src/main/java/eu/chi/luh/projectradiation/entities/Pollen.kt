package eu.chi.luh.projectradiation.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pollen(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "response_pollen") val response: Boolean = false,

    @ColumnInfo(name = "current_pollen") val pollenCurrent: Double? = null,
    @ColumnInfo(name = "average_pollen") val pollenAverage: Double? = null,
    @ColumnInfo(name = "minimum_pollen") val pollenMinimum: Double? = null,
    @ColumnInfo(name = "maximum_pollen") val pollenMaximum: Double? = null,

    @ColumnInfo(name = "current_grass_pollen") val pollenGrassCurrent: Int? = null,
    @ColumnInfo(name = "average_grass_pollen") val pollenGrassAverage: Double? = null,
    @ColumnInfo(name = "minimum_grass_pollen") val pollenGrassMinimum: Int? = null,
    @ColumnInfo(name = "maximum_grass_pollen") val pollenGrassMaximum: Int? = null,

    @ColumnInfo(name = "current_tree_pollen") val pollenTreeCurrent: Int? = null,
    @ColumnInfo(name = "average_tree_pollen") val pollenTreeAverage: Double? = null,
    @ColumnInfo(name = "minimum_tree_pollen") val pollenTreeMinimum: Int? = null,
    @ColumnInfo(name = "maximum_tree_pollen") val pollenTreeMaximum: Int? = null,

    @ColumnInfo(name = "current_weed_pollen") val pollenWeedCurrent: Int? = null,
    @ColumnInfo(name = "average_weed_pollen") val pollenWeedAverage: Double? = null,
    @ColumnInfo(name = "minimum_weed_pollen") val pollenWeedMinimum: Int? = null,
    @ColumnInfo(name = "maximum_weed_pollen") val pollenWeedMaximum: Int? = null,
)
