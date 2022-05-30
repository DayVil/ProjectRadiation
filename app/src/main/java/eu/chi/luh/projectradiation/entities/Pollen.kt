package eu.chi.luh.projectradiation.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pollen(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "response_pollen") val response: Boolean = false,

    @ColumnInfo(name = "current_pollen") val pollenCurrent: Int? = null,
    @ColumnInfo(name = "average_pollen") val pollenAverage: Int? = null,
    @ColumnInfo(name = "minimum_pollen") val pollenMinimum: Int? = null,
    @ColumnInfo(name = "maximum_pollen") val pollenMaximum: Int? = null,

    @ColumnInfo(name = "current_grass_pollen") val pollenGrassCurrent: Int? = null,
    @ColumnInfo(name = "average_grass_pollen") val pollenGrassAverage: Int? = null,
    @ColumnInfo(name = "minimum_grass_pollen") val pollenGrassMinimum: Int? = null,
    @ColumnInfo(name = "maximum_grass_pollen") val pollenGrassMaximum: Int? = null,

    @ColumnInfo(name = "current_tree_pollen") val pollenTreeCurrent: Int? = null,
    @ColumnInfo(name = "average_tree_pollen") val pollenTreeAverage: Int? = null,
    @ColumnInfo(name = "minimum_tree_pollen") val pollenTreeMinimum: Int? = null,
    @ColumnInfo(name = "maximum_tree_pollen") val pollenTreeMaximum: Int? = null
)
