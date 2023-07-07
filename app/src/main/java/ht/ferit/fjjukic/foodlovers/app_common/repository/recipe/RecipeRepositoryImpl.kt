package ht.ferit.fjjukic.foodlovers.app_common.repository.recipe

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step
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
                        Result.success(recipe.mapToRecipeModel(category, difficulty, user))
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

    private fun RecipeModel.mapToRecipe(): Recipe {
        return Recipe(
            this.id,
            this.name,
            this.description,
            this.time,
            this.servings,
            this.steps.map { Step(it.position, it.description) },
            this.ingredients.map { Ingredient(it.name, it.amount) },
            this.difficulty!!.id,
            this.category!!.id,
            this.imagePath,
            this.user!!.userId,
        )
    }

    private fun Recipe.mapToRecipeModel(
        category: CategoryModel,
        difficulty: DifficultyModel,
        user: UserModel
    ): RecipeModel {
        return RecipeModel(
            this.id,
            this.name,
            this.description,
            this.time,
            this.servings,
            this.steps.map { StepModel(it.position, it.description) }.toMutableList(),
            this.ingredients.map { IngredientModel(it.name, it.amount) }.toMutableList(),
            difficulty,
            category,
            user,
            this.imagePath
        )
    }

    override suspend fun getRecipes(isForceLoad: Boolean): Result<List<RecipeModel>> {
        return withContext(Dispatchers.IO) {
            val oldRecipes = db.recipeDao().getAll()

            when {
                hasDayPassed() || isForceLoad -> {
                    val newRecipes = firebaseDB.getRecipes().getOrDefault(listOf())

                    if (newRecipes.isNotEmpty()) {
                        saveRecipes(newRecipes)
                        Result.success(newRecipes)
                    } else {
                        val data = getMappedRecipes(oldRecipes)
                        Result.success(data)
                    }
                }

                oldRecipes.isNotEmpty() -> {
                    val data = getMappedRecipes(oldRecipes)
                    Result.success(data)
                }

                else -> {
                    val newRecipes = firebaseDB.getRecipes().getOrDefault(listOf())
                    saveRecipes(newRecipes)

                    Result.success(newRecipes)
                }
            }
        }
    }

    private suspend fun getMappedRecipes(data: List<Recipe>): List<RecipeModel> {
        val recipes = mutableListOf<RecipeModel>()

        data.forEach {
            val category = categoryRepository.getCategory(it.categoryId).getOrNull()
            val difficulty = difficultyRepository.getDifficulty(it.difficultyId).getOrNull()
            val user = userRepository.getUser(it.userId, true).getOrNull()

            if (category != null && difficulty != null && user != null) {
                recipes.add(it.mapToRecipeModel(category, difficulty, user))
            }
        }
        return recipes
    }

    private fun saveRecipes(data: List<RecipeModel>) {
        data.map { it.mapToRecipe() }.forEach {
            db.recipeDao().insert(it)
        }
        preferenceManager.lastUpdatedRecipes = System.currentTimeMillis()
    }

    override suspend fun createRecipe(recipe: RecipeModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            recipe.imagePath =
                saveRecipeImage(recipe.imagePath.toUri(), recipe.id)
                    ?: return@withContext Result.failure(Exception("Error while saving recipe"))

            val result = firebaseDB.createRecipe(recipe)

            if (result.isFailure) {
                removeRecipeImage(recipe.imagePath)
                result
            } else {
                getRecipes(isForceLoad = true)
                Result.success(true)
            }
        }
    }

    private suspend fun removeRecipeImage(imagePath: String) {
        firebaseStorage.getReference(imagePath).delete().await()
    }

    private suspend fun saveRecipeImage(value: Uri, recipeId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ref = firebaseStorage.reference.child(getRecipePath(recipeId))


//                return@withContext ref.downloadUrl.await().toString()
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

    private fun getRecipePath(id: String) = "images/$id.jpg"

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
}