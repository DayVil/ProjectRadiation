package eu.chi.luh.projectradiation.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AirPollution(
    @PrimaryKey(autoGenerate = true) val apId: Int = 0
)