package ht.ferit.fjjukic.foodlovers.app_common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Difficulty

@Dao
interface DifficultyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(difficulty: Difficulty)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(difficulties: List<Difficulty>)

    @Query("SELECT * FROM difficulty ORDER BY id ASC")
    fun getAll(): List<Difficulty>

    @Query("SELECT * FROM difficulty WHERE id = :id")
    fun get(id: String): Difficulty?

    @Update
    fun update(difficulty: Difficulty)

    @Query("DELETE FROM difficulty WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM difficulty")
    fun deleteAll()
}