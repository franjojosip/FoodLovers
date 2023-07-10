package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToRecipe
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToRecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class RecipeRepositoryImpl(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDB: FirebaseDB,
    private val db: RecipeDatabase,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository,
    private val preferenceManager: PreferenceManager
) : RecipeRepository {

    override suspend fun getRecipe(id: String): Result<RecipeModel?> {
        return withContext(Dispatchers.IO) {
            val recipe = db.recipeDao().get(id)

            when {
                recipe != null -> {
                    val category = categoryRepository.getCategory(recipe.categoryId).getOrNull()
                    val difficulty =
                        difficultyRepository.getDifficulty(recipe.difficultyId).getOrNull()
                    val user = userRepository.getUser(recipe.userId, true).getOrNull()

                    if (category != null && difficulty != null && user != null) {
                        val isFavorite = preferenceManager.favoriteRecipeIds.contains(recipe.id)
                        Result.success(
                            recipe.mapToRecipeModel(
                                category,
                                difficulty,
                                user,
                                isFavorite
                            )
                        )
                    } else {
                        Result.failure(Exception("Error while retrieving data"))
                    }
                }

                else -> {
                    firebaseDB.getRecipe(id).map { model ->
                        if (model != null) {
                            db.recipeDao().insert(model.mapToRecipe())
                        }
                        model
                    }
                }
            }
        }
    }

    private fun hasDayPassed(): Boolean {
        return (System.currentTimeMillis() - preferenceManager.lastUpdatedRecipes) >= TimeUnit.DAYS.toMillis(
            1
        )
    }

    override suspend fun getRecipes(isForceLoad: Boolean): Result<List<RecipeModel>> {
        return withContext(Dispatchers.IO) {
            val oldRecipes = db.recipeDao().getAll()

            when {
                hasDayPassed() || oldRecipes.isEmpty() -> {
                    val newRecipes = firebaseDB.getRecipes().getOrDefault(listOf())

                    if (newRecipes.isNotEmpty()) {
                        saveRecipes(newRecipes)
                        Result.success(newRecipes)
                    } else {
                        val data = getMappedRecipes(oldRecipes)
                        Result.success(data)
                    }
                }

                else -> {
                    Result.success(getMappedRecipes(oldRecipes))
                }
            }
        }
    }

    private suspend fun getMappedRecipes(data: List<Recipe>): List<RecipeModel> {
        val recipes = mutableListOf<RecipeModel>()
        val favoriteRecipesIds = preferenceManager.favoriteRecipeIds

        data.forEach {
            val category = categoryRepository.getCategory(it.categoryId).getOrNull()
            val difficulty = difficultyRepository.getDifficulty(it.difficultyId).getOrNull()
            val user = userRepository.getUser(it.userId, true).getOrNull()

            if (category != null && difficulty != null && user != null) {
                val isFavorite = favoriteRecipesIds.contains(it.id)
                recipes.add(it.mapToRecipeModel(category, difficulty, user, isFavorite))
            }
        }
        return recipes.sortedBy { it.name }
    }

    private fun saveRecipes(data: List<RecipeModel>) {
        db.recipeDao().insertAll(data.map { it.mapToRecipe() })
        preferenceManager.lastUpdatedRecipes = System.currentTimeMillis()
    }

    private fun saveRecipe(data: RecipeModel) {
        db.recipeDao().insert(data.mapToRecipe())
    }

    override suspend fun createRecipe(recipe: RecipeModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            recipe.imagePath =
                saveUserImage(recipe.imagePath.toUri(), recipe.id)
                    ?: return@withContext Result.failure(Exception("Error while saving recipe"))

            val result = firebaseDB.createRecipe(recipe)

            if (result.isFailure) {
                removeRecipeImage(recipe.imagePath)
                result
            } else {
                saveRecipe(recipe)
                Result.success(true)
            }
        }
    }

    private suspend fun removeRecipeImage(imagePath: String) {
        firebaseStorage.getReference(imagePath).delete().await()
    }

    private suspend fun saveUserImage(value: Uri, id: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ref = firebaseStorage.reference.child(getImagePath(id))
                val uploadTask = ref.putFile(value).await()

                when {
                    uploadTask.error != null -> {
                        null
                    }

                    else -> {
                        uploadTask.storage.downloadUrl.await().toString()
                    }
                }
            } catch (e: Exception) {
                Log.d("log", e.message.toString())
                null
            }
        }
    }

    private fun getImagePath(id: String) = "images/$id.jpg"

    override suspend fun updateRecipe(recipe: RecipeModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            db.recipeDao().update(recipe.mapToRecipe())
            firebaseDB.updateRecipe(recipe)
        }
    }

    override suspend fun deleteRecipe(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            db.recipeDao().delete(id)
            firebaseDB.deleteRecipe(id)
        }
    }

    override suspend fun updateRecipeFavorite(id: String, isFavorite: Boolean): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val favoriteRecipesIds = preferenceManager.favoriteRecipeIds.toMutableList()
            if (isFavorite && !favoriteRecipesIds.contains(id)) {
                favoriteRecipesIds.add(id)
            } else if (!isFavorite) {
                favoriteRecipesIds.remove(id)
            }
            preferenceManager.favoriteRecipeIds = favoriteRecipesIds

            Result.success(true)
        }
    }
}