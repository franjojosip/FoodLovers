package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import io.reactivex.rxjava3.core.Observable

class RecipeRepositoryImpl(
    private val firebaseDB: FirebaseDB
) : RecipeRepository {
    override fun getRecipe(id: String): Observable<RecipeModel> = firebaseDB.getRecipe(id)

    override fun getRecipes(): Observable<List<RecipeModel>> {
        // return firebaseDB.getRecipes()

        // Testing recipes
        return firebaseDB.getRecipes().map {
            val data = mutableListOf<RecipeModel>()
            data.apply { addAll(it) }.apply { addAll(it) }.apply { addAll(it) }.apply { addAll(it) }
        }
    }

    override fun insertRecipe(recipe: RecipeModel): Observable<Boolean> =
        firebaseDB.postRecipe(recipe)

    override fun updateRecipe(recipe: RecipeModel): Observable<Boolean> =
        firebaseDB.putRecipe(recipe)

    override fun deleteRecipe(id: String): Observable<Boolean> = firebaseDB.deleteRecipe(id)
}