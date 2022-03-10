package ht.ferit.fjjukic.foodlovers.app_common.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ht.ferit.fjjukic.foodlovers.app_common.model.db.DifficultyLevel

@Dao
interface DifficultyLevelDao {
    @Insert
    fun insert(difficultyLevel: DifficultyLevel)

    @Insert
    fun insertAll(difficultyLevels: List<DifficultyLevel>)

    @Query("SELECT * FROM difficulty_level ORDER BY id ASC")
    fun getAll(): LiveData<List<DifficultyLevel>>

    @Query("SELECT * FROM difficulty_level WHERE id = :id")
    fun get(id: Int): LiveData<DifficultyLevel>

    @Update
    fun update(difficultyLevel: DifficultyLevel)

    @Query("DELETE FROM difficulty_level WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM difficulty_level")
    fun deleteAll()
}