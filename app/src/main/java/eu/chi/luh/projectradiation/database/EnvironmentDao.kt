package eu.chi.luh.projectradiation.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EnvironmentDao {
    @Query("SELECT * FROM uvi")
    fun getAll(): List<Uvi>

    @Insert
    fun insertAll(vararg uvis: Uvi)

    @Delete
    fun delete(uvi: Uvi)
}