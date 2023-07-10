package ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository
) : BaseViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _categories.postValue(categoryRepository.getCategories().getOrDefault(listOf()))
            _difficulties.postValue(difficultyRepository.getDifficulties().getOrDefault(listOf()))
        }
    }

    private val _recipe = RecipeModel()

    private val _categories = MutableLiveData<List<CategoryModel>>()
    val categories: LiveData<List<CategoryModel>> = _categories

    private val _difficulties = MutableLiveData<List<DifficultyModel>>()
    val difficulties: LiveData<List<DifficultyModel>> = _difficulties

    private var _isDataChanged = true
    val isDataChanged = _isDataChanged

    val recipe = _recipe

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _category = MutableLiveData<CategoryModel>()
    val category: LiveData<CategoryModel> = _category

    private val _difficulty = MutableLiveData<DifficultyModel>()
    val difficulty: LiveData<DifficultyModel> = _difficulty

    private val _numOfServings = MutableLiveData(1)
    val numOfServings: LiveData<Int> = _numOfServings

    private val _cookingTime = MutableLiveData(15)
    val cookingTime: LiveData<Int> = _cookingTime

    private val _imagePath = MutableLiveData<Uri>()
    val imagePath: LiveData<Uri> = _imagePath

    val numOfSteps = 4

    val maxServings = 11
    val minServings = 1

    fun onCategoryChanged(category: String) {
        _category.value = categories.value?.first { it.name.equals(category, true) }
        _recipe.category = _category.value
    }

    fun onDifficultyChanged(difficulty: String) {
        _difficulty.value = difficulties.value?.first { it.name.equals(difficulty, true) }
        _recipe.difficulty = _difficulty.value
    }

    fun onNameChanged(text: CharSequence?) {
        _recipe.name = text.toString()
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

    fun setIngredients(values: List<IngredientModel>) {
        _isDataChanged = true
        _recipe.ingredients.apply {
            clear()
            addAll(values)
        }
    }

    fun setSteps(values: List<StepModel>) {
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
            screenEvent.postValue(
                DialogModel(
                    title = R.string.dialog_create_recipe_title,
                    message = R.string.dialog_create_recipe_message,
                    positiveAction = {
                        handleResult({
                            recipeRepository.createRecipe(recipe)
                        }, {
                            screenEvent.postValue(
                                MessageModel(message = "Successfully created recipe")
                            )
                            actionNavigate.postValue(ActionNavigate.Back)
                        }, {
                            screenEvent.postValue(
                                SnackbarModel(message = "Error while creating recipe")
                            )
                        })
                    },
                    positiveTitleId = R.string.dialog_create_recipe_confirm
                )
            )
        } else {
            screenEvent.postValue(
                SnackbarModel(message = "Please check all recipe fields!")
            )
        }
    }

    private fun checkRecipe(): Boolean {
        return recipe.name.isNotBlank() && recipe.ingredients.isNotEmpty() && recipe.steps.isNotEmpty() && recipe.imagePath.isNotBlank()
    }
}