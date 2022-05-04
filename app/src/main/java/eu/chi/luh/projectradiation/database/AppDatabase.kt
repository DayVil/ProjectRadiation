package eu.chi.luh.projectradiation.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Environment::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun environmentDao(): EnvironmentDao
}