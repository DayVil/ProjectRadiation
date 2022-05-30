package eu.chi.luh.projectradiation.entities

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Environment::class], version = 17)
abstract class AppDatabase: RoomDatabase() {
    abstract fun environmentDao(): EnvironmentDao
}