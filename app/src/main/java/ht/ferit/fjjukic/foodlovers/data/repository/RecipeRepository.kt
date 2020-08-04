package ht.ferit.fjjukic.foodlovers.data.repository

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.data.database.RecipeDao
import ht.ferit.fjjukic.foodlovers.data.model.Recipe

class RecipeRepository(
    private val recipeDao: RecipeDao
) {
    fun get(id: Int): LiveData<Recipe> {
        return recipeDao.get(id)
    }

    fun getAll(foodTypeID: Int): LiveData<List<Recipe>> {
        return recipeDao.getAll(foodTypeID)
    }

    fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    fun delete(id:Int) {
        recipeDao.delete(id)
    }

    fun deleteAll() {
        recipeDao.deleteAll()
    }
}