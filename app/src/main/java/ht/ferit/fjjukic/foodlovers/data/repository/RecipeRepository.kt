package ht.ferit.fjjukic.foodlovers.data.repository

import ht.ferit.fjjukic.foodlovers.data.database.FirebaseDB
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback

class RecipeRepository(
    private val firebaseDB: FirebaseDB
) {
    fun getRecipe(id: String, callback: FirebaseDatabaseCallback) = firebaseDB.getRecipe(id, callback)

    fun getRecipes(callback: FirebaseDatabaseCallback) = firebaseDB.getRecipes(callback)

    fun insertRecipe(recipe: RecipeModel) = firebaseDB.postRecipe(recipe)

    fun updateRecipe(recipe: RecipeModel) = firebaseDB.putRecipe(recipe)

    fun deleteRecipe(id: String, callback: FirebaseDatabaseCallback) = firebaseDB.deleteRecipe(id, callback)


    fun getFoodTypes(callback: FirebaseDatabaseCallback) = firebaseDB.getFoodTypes(callback)

    fun insertFoodType(name: String) = firebaseDB.postFoodType(name)

    fun updateFoodType(oldName: String, newName: String) = firebaseDB.putFoodType(oldName, newName)

    fun deleteFoodType(foodTypeId: String) = firebaseDB.deleteFoodType(foodTypeId)


    fun getDifficultyLevels(callback: FirebaseDatabaseCallback) = firebaseDB.getDifficultyLevels(callback)

    fun insertDifficultyLevel(name: String) = firebaseDB.postDifficultyLevel(name)

    fun updateDifficultyLevel(oldName: String, newName: String) = firebaseDB.putDifficultyLevel(oldName, newName)

    fun deleteDifficultyLevel(foodTypeId: String) = firebaseDB.deleteDifficultyLevel(foodTypeId)
}