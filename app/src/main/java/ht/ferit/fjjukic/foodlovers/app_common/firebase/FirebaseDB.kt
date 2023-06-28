package ht.ferit.fjjukic.foodlovers.app_common.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Recipe
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseDB {
    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val usersReference by lazy {
        dbReference.child("users")
    }

    suspend fun createUser(user: UserModel): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = usersReference.child(user.userId)
                reference.setValue(user).await()
                return@withContext Result.success(user)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    suspend fun getUser(userId: String): Result<UserModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = usersReference.child(userId).get().await()
                val user = reference.getValue(UserModel::class.java)

                return@withContext Result.success(user)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    suspend fun updateUser(user: UserModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                usersReference.child(user.userId).setValue(user).await()
                return@withContext Result.success(true)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    suspend fun deleteUser(userId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                usersReference.child(userId).removeValue().await()
                return@withContext Result.success(true)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    suspend fun createRecipe(recipe: Recipe): Boolean {
        return try {
            val reference = dbReference.child("recipes").child(recipe.id)
            reference.setValue(recipe).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun putRecipe(recipe: RecipeModel): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("recipe").orderByChild("id").equalTo(recipe.id)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("recipe")
                                    .child(recipe.id)
                                    .setValue(recipe)
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun deleteRecipe(recipeId: String): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("recipe").orderByKey().equalTo(recipeId)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("recipe").child(recipeId).removeValue()
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    suspend fun getRecipes(): MutableList<Recipe> = withContext(Dispatchers.IO) {
        val data = dbReference.child("recipes").get().await()
        val list = mutableListOf<Recipe>()

        data.children.forEach {
            val model = it.getValue(Recipe::class.java)
            model?.id = it.key.toString()
            model?.let { item -> list.add(item) }
        }

        list
    }

    fun getRecipe(recipeId: String): Observable<RecipeModel> {
        return Observable.create { emitter ->
            dbReference.child("recipe").orderByChild("id").equalTo(recipeId).addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(Throwable(error.message))
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val model = snapshot.children.firstOrNull()?.value
                            model?.let {
                                emitter.onNext(it as RecipeModel)
                            }
                        } else {
                            emitter.onNext(RecipeModel())
                        }
                    }
                }
            )
        }
    }
}