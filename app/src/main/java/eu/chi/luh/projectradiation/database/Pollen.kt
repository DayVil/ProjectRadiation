package eu.chi.luh.projectradiation.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pollen(
    @PrimaryKey(autoGenerate = true) val pollenId: Int = 0
)
