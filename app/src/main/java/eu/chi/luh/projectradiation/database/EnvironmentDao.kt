package eu.chi.luh.projectradiation.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EnvironmentDao {
    @Query("SELECT * FROM environment")
    fun getAll(): List<Environment>

    @Insert
    fun insertEnvironment(env: Environment)

    @Delete
    fun deleteEnvironment(vararg env: Environment)

    @Query("DELETE FROM environment")
    fun deleteAll()
}