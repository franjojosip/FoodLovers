package ht.ferit.fjjukic.foodlovers.data.repository

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.data.database.FoodTypeDao
import ht.ferit.fjjukic.foodlovers.data.model.FoodType

class FoodTypeRepository(
    private val foodTypeDao: FoodTypeDao
) {
    fun get(id: Int): LiveData<FoodType> {
        return foodTypeDao.get(id)
    }

    fun getAll(): LiveData<List<FoodType>> {
        return foodTypeDao.getAll()
    }

    fun insert(foodType: FoodType) {
        foodTypeDao.insert(foodType)
    }

    fun insertAll(foodTypes: List<FoodType>) {
        foodTypeDao.insertAll(foodTypes)
    }

    fun update(foodType: FoodType) {
        foodTypeDao.update(foodType)
    }

    fun delete(id: Int) {
        foodTypeDao.delete(id)
    }

    fun deleteAll() {
        foodTypeDao.deleteAll()
    }
}