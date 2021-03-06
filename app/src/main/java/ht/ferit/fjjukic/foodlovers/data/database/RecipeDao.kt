package ht.ferit.fjjukic.foodlovers.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ht.ferit.fjjukic.foodlovers.data.model.Recipe

@Dao
interface RecipeDao {
    @Insert
    fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipe ORDER BY title ASC")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun get(id: Int): LiveData<Recipe>

    @Update
    fun update(recipe: Recipe)

    @Query("DELETE FROM recipe WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM recipe")
    fun deleteAll()
}