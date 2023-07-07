package ht.ferit.fjjukic.foodlovers.app_common.firebase

import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel

interface FirebaseDB {
    /**
     * Firebase Realtime Database - access users
     */
    suspend fun createUser(user: UserModel): Result<UserModel>
    suspend fun getUser(id: String): Result<UserModel?>
    suspend fun checkEmailExist(email: String): Result<Boolean>
    suspend fun updateUser(user: UserModel): Result<Boolean>
    suspend fun deleteUser(id: String): Result<Boolean>

    /**
     * Firebase Realtime Database - access recipes
     */
    suspend fun createRecipe(recipe: RecipeModel): Result<Boolean>
    suspend fun getRecipe(id: String): Result<RecipeModel?>
    suspend fun getRecipes(): Result<List<RecipeModel>>
    suspend fun updateRecipe(recipe: RecipeModel): Result<Boolean>
    suspend fun deleteRecipe(id: String): Result<Boolean>

    /**
     * Firebase Realtime Database - access categories
     */
    suspend fun createCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun getCategory(id: String): Result<CategoryModel?>
    suspend fun getCategories(): Result<List<CategoryModel>>
    suspend fun updateCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun deleteCategory(id: String): Result<Boolean>

    /**
     * Firebase Realtime Database - access difficulties
     */
    suspend fun createDifficulty(difficulty: DifficultyModel): Result<DifficultyModel>
    suspend fun getDifficulty(id: String): Result<DifficultyModel?>
    suspend fun getDifficulties(): Result<List<DifficultyModel>>
    suspend fun updateDifficulty(difficulty: DifficultyModel): Result<DifficultyModel>
    suspend fun deleteDifficulty(id: String): Result<Boolean>
}