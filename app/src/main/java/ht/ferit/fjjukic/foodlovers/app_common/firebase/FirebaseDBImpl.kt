package ht.ferit.fjjukic.foodlovers.app_common.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FirebaseDBImpl : FirebaseDB {
    companion object {
        const val DB_RECIPES = "recipes"
        const val DB_USERS = "user"
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
                    getUser(recipe.user.userId).getOrNull()?.let {
                        recipe.user = it
                    }
                }

                Result.success(recipe)
            } catch (e: Exception) {
                Result.failure(Exception("Error while getting recipe"))
            }
        }
    }
}