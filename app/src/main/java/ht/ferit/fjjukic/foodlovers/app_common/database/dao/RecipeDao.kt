package ht.ferit.fjjukic.foodlovers.app_common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<Recipe>)

    @Query("SELECT * FROM recipe ORDER BY name ASC")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun get(id: String): Recipe?

    @Update
    fun update(recipe: Recipe)

    @Query("DELETE FROM recipe WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM recipe")
    fun deleteAll()
}