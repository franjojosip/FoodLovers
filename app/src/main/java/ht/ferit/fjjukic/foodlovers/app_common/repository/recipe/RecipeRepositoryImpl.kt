package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Recipe
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RecipeRepositoryImpl(
    private val firebaseDB: FirebaseDB
) : RecipeRepository {

    companion object {
        private const val RECIPE_STORAGE_PATH = "images/"
        private const val IMAGE_TYPE = ".jpg"
    }

    override fun getRecipe(id: String): Observable<RecipeModel> = firebaseDB.getRecipe(id)

    override suspend fun getRecipes() = withContext(Dispatchers.IO) {
        firebaseDB.getRecipes()
    }

    override suspend fun insertRecipe(recipe: Recipe, context: CoroutineContext): Boolean {
        return withContext(context) {
            recipe.imagePath =
                saveRecipeImage(recipe.imagePath.toUri(), recipe.id) ?: return@withContext false

            val isRecipeSaved = firebaseDB.createRecipe(recipe)

            if (!isRecipeSaved) {
                removeRecipeImage(recipe.imagePath)
                return@withContext false
            }

            return@withContext true
        }
    }

    private suspend fun removeRecipeImage(imagePath: String) {
        FirebaseStorage.getInstance().getReference(imagePath).delete().await()
    }

    private suspend fun saveRecipeImage(value: Uri, recipeID: String): String? {
        val ref = FirebaseStorage.getInstance().reference.child(getRecipePath(recipeID))
        val uploadTask = ref.putFile(value).await()

        return when {
            uploadTask.error != null -> {
                null
            }

            else -> {
                uploadTask.storage.downloadUrl.await().toString()
            }
        }
    }

    private fun getRecipePath(id: String): String {
        return RECIPE_STORAGE_PATH + id + IMAGE_TYPE
    }

    override fun updateRecipe(recipe: RecipeModel): Observable<Boolean> =
        firebaseDB.putRecipe(recipe)

    override fun deleteRecipe(id: String): Observable<Boolean> = firebaseDB.deleteRecipe(id)
}