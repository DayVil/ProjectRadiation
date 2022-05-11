package eu.chi.luh.projectradiation.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EnvironmentDao {
    @Query("SELECT * FROM Environment")
    fun getAll(): List<Environment>

    @Query("SELECT * FROM Environment ORDER BY time DESC LIMIT 1")
    fun getLast(): Environment

    @Query("SELECT * FROM Environment LIMIT 1")
    fun checkEmpty(): Environment?


    @Insert
    fun insertAll(vararg env: Environment)


    @Delete
    fun delete(env: Environment)

    @Query("DELETE FROM Environment")
    fun deleteAll()
}