package ht.ferit.fjjukic.foodlovers.data.database

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.*
import ht.ferit.fjjukic.foodlovers.data.model.DifficultyLevel

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