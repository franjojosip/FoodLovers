package ht.ferit.fjjukic.foodlovers.app_common.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel
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

    fun postDifficultyLevel(name: String): Observable<Boolean> {
        return Observable.create { emitter ->
            val difficultyLevel: Map<String, String> = hashMapOf("name" to name)
            dbReference.child("difficulty_level").orderByChild("name").equalTo(name)
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                dbReference.child("difficulty_level").push()
                                    .setValue(difficultyLevel)
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun putDifficultyLevel(oldName: String, newName: String): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("difficulty_level").orderByChild("name").equalTo(oldName)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("difficulty_level")
                                    .child(snapshot.children.first().key.toString()).child("name")
                                    .setValue(newName)
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun deleteDifficultyLevel(name: String): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("difficulty_level").orderByChild("name").equalTo(name)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("difficulty_level")
                                    .child(snapshot.children.first().key.toString()).removeValue()
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun getDifficultyLevels(): Observable<List<BaseModel>> {
        return Observable.create { emitter ->
            dbReference.child("difficulty_level").orderByValue().addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(Throwable(error.message))
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<BaseModel>()
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                it.children.forEach { p ->
                                    list.add(BaseModel(it.key.toString(), p.value.toString()))
                                }
                            }
                        }
                        emitter.onNext(list)
                    }
                }
            )
        }
    }

    fun postFoodType(name: String): Observable<Boolean> {
        return Observable.create { emitter ->
            val difficultyLevel: Map<String, String> = hashMapOf("name" to name)
            dbReference.child("food_type").orderByChild("name").equalTo(name)
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                dbReference.child("food_type").push().setValue(difficultyLevel)
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun putFoodType(oldName: String, newName: String): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("food_type").orderByChild("name").equalTo(oldName)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("food_type")
                                    .child(snapshot.children.first().key.toString()).child("name")
                                    .setValue(newName)
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun deleteFoodType(name: String): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("food_type").orderByChild("name").equalTo(name)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("food_type")
                                    .child(snapshot.children.first().key.toString()).removeValue()
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun getFoodTypes(): Observable<List<BaseModel>> {
        return Observable.create { emitter ->
            dbReference.child("food_type").orderByValue().addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(Throwable(error.message))
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<BaseModel>()
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                it.children.forEach { p ->
                                    list.add(BaseModel(it.key.toString(), p.value.toString()))
                                }
                            }
                        }
                        emitter.onNext(list)
                    }
                }
            )
        }
    }

    suspend fun createUser(user: UserModel): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val reference = dbReference.child("users").child(user.userId)
                reference.setValue(user).await()
                return@withContext Result.success(user)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    fun putUser(user: UserModel): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("user").orderByChild("userId").equalTo(user.userId)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                dbReference.child("user")
                                    .child(user.id).setValue(user)
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    fun deleteUser(userId: String): Observable<Boolean> {
        return Observable.create { emitter ->
            dbReference.child("user").orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(Throwable(error.message))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.ref.removeValue()
                                emitter.onNext(true)
                            } else {
                                emitter.onNext(false)
                            }
                        }
                    }
                )
        }
    }

    suspend fun getUser(userId: String): UserModel? {
        return try {
            val user =
                dbReference.child("user").orderByChild("userId").equalTo(userId).get().await()
            user.getValue(UserModel::class.java)
        } catch (e: Exception) {
            null
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