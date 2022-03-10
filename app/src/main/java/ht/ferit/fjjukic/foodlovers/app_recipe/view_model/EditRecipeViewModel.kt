package ht.ferit.fjjukic.foodlovers.app_recipe.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionResultModel
import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepository
import ht.ferit.fjjukic.foodlovers.app_common.listener.ResultListener
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable

class EditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val resourceRepository: ResourceRepository
) : BaseViewModel() {
    private val _recipe: MutableLiveData<RecipeModel> = MutableLiveData()
    private val _foodType: MutableLiveData<BaseModel> = MutableLiveData()
    private val _difficultyLevel: MutableLiveData<BaseModel> = MutableLiveData()
    private val _difficultyLevels: MutableLiveData<List<BaseModel>> = MutableLiveData()
    private val _foodTypes: MutableLiveData<List<BaseModel>> = MutableLiveData()

    private val _resultModel = SingleLiveData<ActionResultModel>()

    val recipe: LiveData<RecipeModel> = _recipe
    val difficultyLevels: LiveData<List<BaseModel>> = _difficultyLevels
    val foodTypes: LiveData<List<BaseModel>> = _foodTypes

    val resultModel: LiveData<ActionResultModel> = _resultModel

    fun init(recipeID: String) {
        val listener = object: ResultListener {
            override fun <T> onSuccess(value: T) {
                value?.let {
                    if(value is RecipeModel){
                        _recipe.postValue(value!!)
                    }
                }
            }
            override fun onFailure(message: String) {
                _resultModel.postValue(ActionResultModel(false, message))
            }
        }
        recipeRepository.getRecipe(recipeID)
    }

    fun modifyRecipe() {
        val currentRecipe = recipe.value
        currentRecipe!!.userId = "FirebaseSource().currentUser()!!.uid"
        when {
            currentRecipe.title.isNotEmpty() && currentRecipe.description.isNotEmpty() && currentRecipe.imagePath.isNotEmpty() && currentRecipe.foodTypeID != "-1" && currentRecipe.difficultyLevelID != "-1" -> {
                val fileName = currentRecipe.imageId
                val ref = FirebaseStorage.getInstance().getReference("images/$fileName.jpg")
                ref.putFile(currentRecipe.imagePath.toUri()).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        currentRecipe.imagePath = it.toString()
                        recipeRepository.updateRecipe(currentRecipe)
                    }
                }
            }
            else -> _resultModel.postValue(ActionResultModel(false, "Please check all fields!"))
        }
    }

    fun getFoodTypes() {
        val listener = object: ResultListener {
            override fun <T> onSuccess(value: T) {
                value?.let {
                    if(value is RecipeModel){
                        _recipe.postValue(value!!)
                    }
                }
            }

            override fun onFailure(message: String) {
                _resultModel.postValue(ActionResultModel(false, message))
            }
        }
    }

    fun setFoodType(foodType: BaseModel) {
        _recipe.value?.foodTypeID = foodType.id
        _foodType.postValue(foodType)
    }

    fun setDifficultyLevel(difficultyLevel: BaseModel) {
        _recipe.value?.difficultyLevelID = difficultyLevel.id
        _difficultyLevel.value = difficultyLevel
    }

    fun setImagePath(path: String) {
        recipe.value?.imagePath = path
    }

    private fun Completable.subscribeWithResult(onSuccess: () -> Unit): Disposable {
        return this.subscribe({
            onSuccess()
        }, {
            showResult(message = it.message ?: resourceRepository.getString(R.string.general_error_server))
        })
    }

    private fun showResult(isSuccess: Boolean = false, message: String) {
        _resultModel.postValue(ActionResultModel(isSuccess, message))
    }
}