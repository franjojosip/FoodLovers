package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.BaseModel
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.data.repository.RecipeRepository
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback
import java.util.*


class RecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    var recipe: MutableLiveData<RecipeModel> = MutableLiveData()
    var foodType: MutableLiveData<BaseModel> = MutableLiveData()
    var difficultyLevel: MutableLiveData<BaseModel> = MutableLiveData()
    var authListener: AuthListener? = null

    var difficultyLevels: MutableLiveData<List<BaseModel>> =
        MutableLiveData<List<BaseModel>>()
    var foodTypes: MutableLiveData<List<BaseModel>> = MutableLiveData<List<BaseModel>>()
    private var recipes: MutableLiveData<List<RecipeModel>> = MutableLiveData<List<RecipeModel>>()

    var actionType: String = "Add"
    var recipeID: String = "-1"

    fun getRecipe(recipeID: String): LiveData<RecipeModel> {
        recipeRepository.getRecipe(recipeID, object : FirebaseDatabaseCallback {
            override fun <T : Any> onCallback(value: T?) {
                recipe.value = value as RecipeModel
            }
        })
        return recipe
    }

    fun getRecipes(): LiveData<List<RecipeModel>> {
        recipeRepository.getRecipes(object : FirebaseDatabaseCallback {
            override fun <T : Any> onCallback(value: T?) {
                @Suppress("UNCHECKED_CAST")
                recipes.value = value as List<RecipeModel>
            }
        })
        return recipes
    }

    fun deleteRecipe(id: String, imageId: String) {
        recipeRepository.deleteRecipe(id, object : FirebaseDatabaseCallback {
            override fun <T : Any> onCallback(value: T?) {
                if(value as Boolean){
                    val photoRef: StorageReference = FirebaseStorage.getInstance().getReference("images/$imageId.jpg")
                    photoRef.delete()
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                            Log.d("Fail", "onFailure: did not delete file")
                        }
                }
            }
        })
    }

    fun modifyRecipe() {
        val currentRecipe = recipe.value
        currentRecipe!!.userId = FirebaseSource().currentUser()!!.uid
        when {
            currentRecipe.title.isNotEmpty() && currentRecipe.description.isNotEmpty() && currentRecipe.imagePath.isNotEmpty() && currentRecipe.foodTypeID != "-1" && currentRecipe.difficultyLevelID != "-1" -> {
                when {
                    recipeID != "-1" -> {
                        val fileName = currentRecipe.imageId
                        val ref = FirebaseStorage.getInstance().getReference("images/$fileName.jpg")
                        ref.putFile(currentRecipe.imagePath.toUri()).addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener {
                                currentRecipe.imagePath = it.toString()
                                recipeRepository.updateRecipe(currentRecipe)
                            }
                        }
                        authListener?.onSuccess()
                    }
                    else -> {
                        val fileName = UUID.randomUUID().toString()
                        val ref = FirebaseStorage.getInstance().getReference("images/$fileName.jpg")
                        ref.putFile(currentRecipe.imagePath.toUri()).addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener {
                                currentRecipe.imagePath = it.toString()
                                currentRecipe.imageId = fileName
                                recipeRepository.insertRecipe(currentRecipe)
                            }
                        }
                        authListener?.onSuccess()
                    }
                }
            }
            else -> authListener?.onFailure("Please check all fields!")
        }
    }

    fun getFoodTypes(): LiveData<List<BaseModel>> {
        if (foodTypes.value == null) {
            recipeRepository.getFoodTypes(object : FirebaseDatabaseCallback {
                override fun <T : Any> onCallback(value: T?) {
                    @Suppress("UNCHECKED_CAST")
                    foodTypes.value = value as List<BaseModel>
                    if (recipe.value != null && foodType.value == null && recipe.value!!.foodTypeID != "-1") {
                        val item =
                            foodTypes.value!!.first { item -> item.id.contains(recipe.value!!.foodTypeID) }
                        setFoodType(item)
                    }
                }
            })
        }
        return foodTypes
    }

    fun setFoodType(foodType: BaseModel) {
        recipe.value!!.foodTypeID = foodType.id
        this.foodType.value = foodType
    }

    fun getDifficultyLevels(): LiveData<List<BaseModel>> {
        if (difficultyLevels.value == null) {
            recipeRepository.getDifficultyLevels(object : FirebaseDatabaseCallback {
                override fun <T : Any> onCallback(value: T?) {
                    @Suppress("UNCHECKED_CAST")
                    difficultyLevels.value = value as List<BaseModel>
                    if (recipe.value != null && difficultyLevel.value == null && recipe.value!!.difficultyLevelID != "-1") {
                        val item =
                            difficultyLevels.value!!.first { item -> item.id.contains(recipe.value!!.difficultyLevelID) }
                        setDifficultyLevel(item)
                    }
                }
            })
        }
        return difficultyLevels
    }

    fun setDifficultyLevel(difficultyLevel: BaseModel) {
        recipe.value!!.difficultyLevelID = difficultyLevel.id
        this.difficultyLevel.value = difficultyLevel
    }

    fun setImagePath(path: String) {
        recipe.value!!.imagePath = path
    }

}