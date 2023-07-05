package ht.ferit.fjjukic.foodlovers.app_common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM category ORDER BY id ASC")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE id = :id")
    fun get(id: String): Category?

    @Update
    fun update(Category: Category)

    @Query("DELETE FROM category WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM category")
    fun deleteAll()
}