package ht.ferit.fjjukic.foodlovers.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ht.ferit.fjjukic.foodlovers.data.model.FoodType

@Dao
interface FoodTypeDao {
    @Insert
    fun insert(foodType: FoodType)

    @Insert
    fun insertAll(foodTypes: List<FoodType>)

    @Query("SELECT * FROM food_type ORDER BY id ASC")
    fun getAll(): LiveData<List<FoodType>>

    @Query("SELECT id,name FROM food_type WHERE id = :id")
    fun get(id: Int): LiveData<FoodType>

    @Update
    fun update(foodType: FoodType)

    @Query("DELETE FROM food_type WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM food_type")
    fun deleteAll()
}