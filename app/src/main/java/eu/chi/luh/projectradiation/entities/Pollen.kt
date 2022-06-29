package eu.chi.luh.projectradiation.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pollen(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "response_pollen") var response: Boolean = false,

    @ColumnInfo(name = "current_pollen") var pollenCurrent: Double? = null,
    @ColumnInfo(name = "average_pollen") var pollenAverage: Double? = null,
    @ColumnInfo(name = "minimum_pollen") var pollenMinimum: Double? = null,
    @ColumnInfo(name = "maximum_pollen") var pollenMaximum: Double? = null,

    @ColumnInfo(name = "current_grass_pollen") var pollenGrassCurrent: Int? = null,
    @ColumnInfo(name = "average_grass_pollen") var pollenGrassAverage: Double? = null,
    @ColumnInfo(name = "minimum_grass_pollen") var pollenGrassMinimum: Int? = null,
    @ColumnInfo(name = "maximum_grass_pollen") var pollenGrassMaximum: Int? = null,

    @ColumnInfo(name = "current_tree_pollen") var pollenTreeCurrent: Int? = null,
    @ColumnInfo(name = "average_tree_pollen") var pollenTreeAverage: Double? = null,
    @ColumnInfo(name = "minimum_tree_pollen") var pollenTreeMinimum: Int? = null,
    @ColumnInfo(name = "maximum_tree_pollen") var pollenTreeMaximum: Int? = null,

    @ColumnInfo(name = "current_weed_pollen") var pollenWeedCurrent: Int? = null,
    @ColumnInfo(name = "average_weed_pollen") var pollenWeedAverage: Double? = null,
    @ColumnInfo(name = "minimum_weed_pollen") var pollenWeedMinimum: Int? = null,
    @ColumnInfo(name = "maximum_weed_pollen") var pollenWeedMaximum: Int? = null,
)
