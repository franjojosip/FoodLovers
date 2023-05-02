package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.IngredientUI
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NewRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.StepUI

class CreateRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val resourceRepository: ResourceRepository
) : BaseViewModel() {
    val author = "By Random Guy" // TEST PURPOSE ONLY

    private val _recipe = NewRecipe()

    private var _isDataChanged = true
    val isDataChanged = _isDataChanged

    val recipe = _recipe

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _numOfServings = MutableLiveData(1)
    val numOfServings: LiveData<Int> = _numOfServings

    private val _cookingTime = MutableLiveData(15)
    val cookingTime: LiveData<Int> = _cookingTime

    private val _imagePath = MutableLiveData<Uri>()
    val imagePath: LiveData<Uri> = _imagePath

    val recipeTabs = listOf("Ingredients", "Steps")

    val numOfSteps = 4

    fun onNameChanged(text: CharSequence?) {
        _title.value = text.toString()
        _isDataChanged = true
    }

    fun onChangeCounter(isIncrement: Boolean) {
        _isDataChanged = true
        when {
            isIncrement && _recipe.servings < 11 -> {
                _recipe.servings++
                _numOfServings.value = _recipe.servings
            }
            _recipe.servings > 1 -> {
                _recipe.servings--
                _numOfServings.value = _recipe.servings
            }
        }
    }

    fun onChangeTime(value: Int) {
        _isDataChanged = true
        _recipe.time = value
        _cookingTime.value = value
    }

    fun getCookingTime(): Float {
        return _recipe.time.toFloat()
    }

    fun onImageSelected(imageURI: Uri) {
        _isDataChanged = true
        _recipe.imageURI = imageURI.toString()
        _imagePath.value = imageURI
    }

    fun setIngredients(values: List<IngredientUI>) {
        _isDataChanged = true
        _recipe.ingredients.apply {
            clear()
            addAll(values)
        }
    }

    fun setSteps(values: List<StepUI>) {
        _isDataChanged = true
        _recipe.steps.apply {
            clear()
            addAll(values)
        }
    }

    fun isRecipeFilled(): Boolean {
        return !recipe.name.isNullOrEmpty() && recipe.ingredients.isNotEmpty() && recipe.steps.isNotEmpty()
    }

    fun setDataChanged(value: Boolean) {
        _isDataChanged = value
    }

    fun confirmCreateRecipe() {
        
    }

}