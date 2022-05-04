package eu.chi.luh.projectradiation.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Environment(
    @PrimaryKey val dt: Long = System.currentTimeMillis(),
    @Embedded val uvi: Uvi
)
