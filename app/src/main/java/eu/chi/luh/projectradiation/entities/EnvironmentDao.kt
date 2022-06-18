package eu.chi.luh.projectradiation.entities

import androidx.room.*

@Dao
interface EnvironmentDao {
    @Query("SELECT * FROM Environment ORDER BY time DESC")
    fun getAll(): List<Environment>

    @Query("SELECT * FROM Environment ORDER BY time DESC LIMIT 1")
    fun getLast(): Environment

    @Query("SELECT * FROM Environment LIMIT 1")
    fun checkEmpty(): Environment?

    @Query("SELECT COUNT(time) FROM Environment")
    fun getCount(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg env: Environment)


    @Delete
    fun delete(env: Environment)

    @Query("DELETE FROM Environment")
    fun deleteAll()
}