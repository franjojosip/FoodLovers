package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import io.reactivex.rxjava3.core.Observable

interface RecipeRepository {
    fun getRecipe(id: String): Observable<RecipeModel>

    fun getRecipes(): Observable<List<RecipeModel>>

    fun insertRecipe(recipe: RecipeModel): Observable<Boolean>

    fun updateRecipe(recipe: RecipeModel): Observable<Boolean>

    fun deleteRecipe(id: String): Observable<Boolean>
}