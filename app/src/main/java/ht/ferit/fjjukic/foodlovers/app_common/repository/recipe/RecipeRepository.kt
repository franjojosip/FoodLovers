package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Recipe
import io.reactivex.rxjava3.core.Observable
import kotlin.coroutines.CoroutineContext

interface RecipeRepository {
    fun getRecipe(id: String): Observable<RecipeModel>

    suspend fun getRecipes(): List<Recipe>

    suspend fun insertRecipe(recipe: Recipe, context: CoroutineContext): Boolean

    fun updateRecipe(recipe: RecipeModel): Observable<Boolean>

    fun deleteRecipe(id: String): Observable<Boolean>
}