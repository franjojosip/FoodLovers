package ht.ferit.fjjukic.foodlovers.app_common.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FirebaseDBImpl : FirebaseDB {
    companion object {
        const val DB_RECIPES = "recipes"
        const val DB_USERS = "users"
        const val DB_CATEGORIES = "categories"
        const val DB_DIFFICULTIES = "difficulties"
    }

    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val usersReference by lazy {
        dbReference.child(DB_USERS)
    }

    private val recipesReference by lazy {
        dbReference.child(DB_RECIPES)
    }

    private val categoriesReference by lazy {
        dbReference.child(DB_CATEGORIES)
    }

    private val difficultiesReference by lazy {
        dbReference.child(DB_DIFFICULTIES)
    }

    override suspend fun createUser(user: UserModel): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = usersReference.child(user.userId)
                reference.setValue(user).await()
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(Exception("Error while creating user"))
            }
        }
    }

    override suspend fun getUser(id: String): Result<UserModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = usersReference.child(id).get().await()
                val user = reference.getValue(UserModel::class.java)

                Result.success(user)
            } catch (e: Exception) {
                Result.failure(Exception("Error while getting user"))
            }
        }
    }

    override suspend fun checkEmailExist(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val data = usersReference.orderByChild("email").get().await()
                val emails = mutableListOf<String>()

                data.children.forEach { child ->
                    child.getValue(UserModel::class.java)?.let {
                        emails.add(it.email)
                    }
                }
                when (emails.contains(email)) {
                    true -> Result.success(true)
                    else -> Result.failure(Exception("Email doesn't exist!"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error while getting user"))
            }
        }
    }

    override suspend fun updateUser(user: UserModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                usersReference.child(user.userId).setValue(user).await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while updating user"))
            }
        }
    }

    override suspend fun deleteUser(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                usersReference.child(id).removeValue().await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while deleting user"))
            }
        }
    }

    override suspend fun createRecipe(recipe: RecipeModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                recipesReference.child(recipe.id).setValue(recipe).await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while creating recipe"))
            }
        }
    }

    override suspend fun updateRecipe(recipe: RecipeModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                recipesReference.child(recipe.id).setValue(recipe).await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while updating recipe"))
            }
        }
    }

    override suspend fun deleteRecipe(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                recipesReference.child(id).removeValue().await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while deleting recipe"))
            }
        }
    }

    override suspend fun getRecipes(): Result<List<RecipeModel>> {
        return withContext(Dispatchers.IO) {
            val data = recipesReference.get().await()
            val list = mutableListOf<RecipeModel>()

            data.children.forEach { child ->
                child.getValue(RecipeModel::class.java)?.let {
                    list.add(it)
                }
            }

            Result.success(list)
        }
    }

    override suspend fun getRecipe(id: String): Result<RecipeModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = recipesReference.child(id).get().await()
                val recipe = reference.getValue(RecipeModel::class.java)

                if (recipe != null) {
                    recipe.user?.userId?.let {
                        getUser(it).getOrNull()?.let {
                            recipe.user = it
                        }
                    }
                }

                Result.success(recipe)
            } catch (e: Exception) {
                Result.failure(Exception("Error while getting recipe"))
            }
        }
    }

    override suspend fun createCategory(category: CategoryModel): Result<CategoryModel> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = categoriesReference.child(category.id)
                reference.setValue(category).await()
                Result.success(category)
            } catch (e: Exception) {
                Result.failure(Exception("Error while creating category"))
            }
        }
    }

    override suspend fun getCategory(id: String): Result<CategoryModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = categoriesReference.child(id).get().await()
                val category = reference.getValue(CategoryModel::class.java)
                Result.success(category)
            } catch (e: Exception) {
                Result.failure(Exception("Error while deleting category"))
            }
        }
    }

    override suspend fun getCategories(): Result<List<CategoryModel>> {
        return withContext(Dispatchers.IO) {
            val data = categoriesReference.get().await()
            val list = mutableListOf<CategoryModel>()

            data.children.forEach { child ->
                child.getValue(CategoryModel::class.java)?.let {
                    list.add(it)
                }
            }

            Result.success(list)
        }
    }

    override suspend fun updateCategory(category: CategoryModel): Result<CategoryModel> {
        return withContext(Dispatchers.IO) {
            try {
                usersReference.child(category.id).setValue(category).await()
                Result.success(category)
            } catch (e: Exception) {
                Result.failure(Exception("Error while updating category"))
            }
        }
    }

    override suspend fun deleteCategory(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                categoriesReference.child(id).removeValue().await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while deleting category"))
            }
        }
    }

    override suspend fun createDifficulty(difficulty: DifficultyModel): Result<DifficultyModel> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = difficultiesReference.child(difficulty.id)
                reference.setValue(difficulty).await()
                Result.success(difficulty)
            } catch (e: Exception) {
                Result.failure(Exception("Error while creating difficulty"))
            }
        }
    }

    override suspend fun getDifficulty(id: String): Result<DifficultyModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = difficultiesReference.child(id).get().await()
                val difficulty = reference.getValue(DifficultyModel::class.java)
                Result.success(difficulty)
            } catch (e: Exception) {
                Result.failure(Exception("Error while deleting difficulty"))
            }
        }
    }

    override suspend fun getDifficulties(): Result<List<DifficultyModel>> {
        return withContext(Dispatchers.IO) {
            val data = difficultiesReference.get().await()
            val list = mutableListOf<DifficultyModel>()

            data.children.forEach { child ->
                child.getValue(DifficultyModel::class.java)?.let {
                    list.add(it)
                }
            }

            Result.success(list)
        }
    }

    override suspend fun updateDifficulty(difficulty: DifficultyModel): Result<DifficultyModel> {
        return withContext(Dispatchers.IO) {
            try {
                usersReference.child(difficulty.id).setValue(difficulty).await()
                Result.success(difficulty)
            } catch (e: Exception) {
                Result.failure(Exception("Error while updating difficulty"))
            }
        }
    }

    override suspend fun deleteDifficulty(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                difficultiesReference.child(id).removeValue().await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Error while deleting difficulty"))
            }
        }
    }
}