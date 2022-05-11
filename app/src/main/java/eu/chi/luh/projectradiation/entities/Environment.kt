package eu.chi.luh.projectradiation.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Environment(
    @PrimaryKey val time: Long,
    @Embedded val uvi: Uvi?
//    @Embedded val pollen: Pollen?,
//    @Embedded val airPollution: AirPollution?
)
