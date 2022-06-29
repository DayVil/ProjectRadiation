package eu.chi.luh.projectradiation.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Environment::class], version = 32)
abstract class ProjectRadiationDatabase: RoomDatabase() {
    abstract fun environmentDao(): EnvironmentDao

    // To make a singleton
    companion object {
        @Volatile private var instance: ProjectRadiationDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProjectRadiationDatabase::class.java,
                "environment-data"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}