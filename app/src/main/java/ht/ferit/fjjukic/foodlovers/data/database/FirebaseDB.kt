package ht.ferit.fjjukic.foodlovers.data.database

import com.google.firebase.database.*
import ht.ferit.fjjukic.foodlovers.data.model.BaseModel
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.data.model.UserModel
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback

class FirebaseDB {
    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    fun postDifficultyLevel(name: String) {
        val difficultyLevel: Map<String, String> = hashMapOf("name" to name)
        dbReference.child("difficulty_level").orderByChild("name").equalTo(name)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            dbReference.child("difficulty_level").push().setValue(difficultyLevel)
                        }
                    }
                }
            )
    }

    fun putDifficultyLevel(oldName: String, newName: String) {
        dbReference.child("difficulty_level").orderByChild("name").equalTo(oldName)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("difficulty_level")
                                .child(snapshot.children.first().key.toString()).child("name")
                                .setValue(newName)
                        }
                    }
                }
            )
    }

    fun deleteDifficultyLevel(name: String) {
        dbReference.child("difficulty_level").orderByChild("name").equalTo(name)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("difficulty_level")
                                .child(snapshot.children.first().key.toString()).removeValue()
                        }
                    }
                }
            )
    }

    fun getDifficultyLevels(callback: FirebaseDatabaseCallback) {
        dbReference.child("difficulty_level").orderByValue().addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
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
                    callback.onCallback(list)
                }
            }
        )
    }

    fun postFoodType(name: String) {
        val difficultyLevel: Map<String, String> = hashMapOf("name" to name)
        dbReference.child("food_type").orderByChild("name").equalTo(name)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            dbReference.child("food_type").push().setValue(difficultyLevel)
                        }
                    }
                }
            )
    }

    fun putFoodType(oldName: String, newName: String) {
        dbReference.child("food_type").orderByChild("name").equalTo(oldName)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("food_type")
                                .child(snapshot.children.first().key.toString()).child("name")
                                .setValue(newName)
                        }
                    }
                }
            )
    }

    fun deleteFoodType(name: String) {
        dbReference.child("food_type").orderByChild("name").equalTo(name)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("food_type")
                                .child(snapshot.children.first().key.toString()).removeValue()
                        }
                    }
                }
            )
    }

    fun getFoodTypes(callback: FirebaseDatabaseCallback) {
        dbReference.child("food_type").orderByValue().addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
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
                    callback.onCallback(list)
                }
            }
        )
    }

    fun postUser(user: UserModel) {
        dbReference.child("user").orderByChild("email").equalTo(user.email)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            val key = dbReference.child("user").push().key
                            user.id = key.toString()
                            dbReference.child("user").child(key!!).setValue(user)
                        }
                    }
                }
            )
    }

    fun putUser(user: UserModel) {
        dbReference.child("user").orderByChild("userId").equalTo(user.userId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("user")
                                .child(user.id).setValue(user)
                        }
                    }
                }
            )
    }

    fun deleteUser(userId: String) {
        dbReference.child("user").orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.ref.removeValue()
                        }
                    }
                }
            )
    }

    fun getUser(userId: String, callback: FirebaseDatabaseCallback) {
        dbReference.child("user").orderByChild("userId").equalTo(userId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val model = it.getValue(UserModel::class.java)
                                model!!.id = it.key.toString()
                                callback.onCallback(model)
                            }
                        }
                    }
                }
            )
    }

    fun postRecipe(recipe: RecipeModel) {
        dbReference.child("recipe").orderByChild("id").equalTo(recipe.id)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            val key = dbReference.child("recipe").push().key
                            recipe.id = key.toString()
                            dbReference.child("recipe").child(key!!).setValue(recipe)
                        }
                    }
                }
            )
    }

    fun putRecipe(recipe: RecipeModel) {
        dbReference.child("recipe").orderByChild("id").equalTo(recipe.id)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("recipe")
                                .child(recipe.id)
                                .setValue(recipe)
                        }
                    }
                }
            )
    }

    fun deleteRecipe(recipeId: String, callback: FirebaseDatabaseCallback) {
        dbReference.child("recipe").orderByKey().equalTo(recipeId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            dbReference.child("recipe").child(recipeId).removeValue()
                            callback.onCallback(true)
                        }
                        callback.onCallback(false)
                    }
                }
            )
    }

    fun getRecipes(callback: FirebaseDatabaseCallback) {
        dbReference.child("recipe").orderByValue().addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<RecipeModel>()
                    if (snapshot.exists() && snapshot.children.count() > 0) {
                        snapshot.children.forEach {
                            val model = it.getValue(RecipeModel::class.java)
                            model!!.id = it.key.toString()
                            list.add(model)
                        }
                    }
                    callback.onCallback(list)
                }
            }
        )
    }

    fun getRecipe(recipeId: String, callback: FirebaseDatabaseCallback) {
        dbReference.child("recipe").orderByChild("id").equalTo(recipeId).addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val model = it.getValue(RecipeModel::class.java)
                            callback.onCallback(model)
                        }
                    }
                }
            }
        )
    }
}