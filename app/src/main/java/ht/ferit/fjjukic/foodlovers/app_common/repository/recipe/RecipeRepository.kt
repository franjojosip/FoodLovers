package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel

interface RecipeRepository {
    suspend fun getRecipe(id: String): Result<RecipeModel?>
    suspend fun getRecipes(isForceLoad: Boolean = false): Result<List<RecipeModel>>
    suspend fun createRecipe(recipe: RecipeModel): Result<Boolean>
    suspend fun updateRecipe(recipe: RecipeModel): Result<Boolean>
    suspend fun deleteRecipe(id: String): Result<Boolean>
    suspend fun updateRecipeFavorite(id: String, isFavorite: Boolean): Result<Boolean>
}