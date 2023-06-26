package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Recipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {
    private val _recipe = Recipe(author = "By Jane Doe")

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

    val numOfSteps = 4

    val maxServings = 11
    val minServings = 1

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

            !isIncrement && _recipe.servings > 1 -> {
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
        _recipe.imagePath = imageURI.toString()
        _imagePath.value = imageURI
    }

    fun setIngredients(values: List<Ingredient>) {
        _isDataChanged = true
        _recipe.ingredients.apply {
            clear()
            addAll(values)
        }
    }

    fun setSteps(values: List<Step>) {
        _isDataChanged = true
        _recipe.steps.apply {
            clear()
            addAll(values)
        }
    }

    fun setDataChanged(value: Boolean) {
        _isDataChanged = value
    }

    fun createRecipe() {
        if (checkRecipe()) {
            _screenEvent.postValue(
                DialogModel(
                    title = R.string.dialog_create_recipe_title,
                    message = R.string.dialog_create_recipe_message,
                    positiveAction = {
                        confirmCreateRecipe()
                    },
                    positiveTitleId = R.string.dialog_create_recipe_confirm
                )
            )
        } else {
            _screenEvent.postValue(
                SnackbarModel(message = "Please check all recipe fields!")
            )
        }
    }

    private fun checkRecipe(): Boolean {
        return recipe.name.isNotBlank() && recipe.ingredients.isNotEmpty() && recipe.steps.isNotEmpty() && recipe.imagePath.isNotBlank()
    }

    fun confirmCreateRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            _showLoading.postValue(true)
            recipeRepository.insertRecipe(recipe, coroutineContext)
            _showLoading.postValue(false)
        }
    }
}