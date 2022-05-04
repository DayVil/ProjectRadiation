package eu.chi.luh.projectradiation.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Uvi::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun environmentDao(): EnvironmentDao
}