package ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository
) : BaseViewModel() {

    private val _oldRecipe: MutableLiveData<RecipeModel> = MutableLiveData()
    val oldRecipe: LiveData<RecipeModel> = _oldRecipe

    var newRecipe = RecipeModel()
        private set

    private val _categories = MutableLiveData<List<CategoryModel>>()
    val categories: LiveData<List<CategoryModel>> = _categories

    private val _difficulties = MutableLiveData<List<DifficultyModel>>()
    val difficulties: LiveData<List<DifficultyModel>> = _difficulties

    val dataChanged: MutableLiveData<Boolean> = MutableLiveData()

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _category = MutableLiveData<CategoryModel>()
    val category: LiveData<CategoryModel> = _category

    private val _difficulty = MutableLiveData<DifficultyModel>()
    val difficulty: LiveData<DifficultyModel> = _difficulty

    val numOfSteps = 4

    val maxServings = 11
    val minServings = 1
    fun init(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = categoryRepository.getCategories().getOrDefault(listOf())
            val difficulties = difficultyRepository.getDifficulties().getOrDefault(listOf())

            withContext(Dispatchers.Main) {
                _categories.value = categories
                _difficulties.value = difficulties
            }
        }

        handleResult({
            when {
                id != null -> recipeRepository.getRecipe(id)
                else -> Result.success(null)
            }
        }, {
            if (it != null) {
                withContext(Dispatchers.Main) {
                    _oldRecipe.value = it
                    newRecipe = it
                }
            } else {
                actionNavigate.postValue(ActionNavigate.Back)
            }
        }, {
            screenEvent.postValue(
                SnackbarModel(message = "Error while loading recipe")
            )
            actionNavigate.postValue(ActionNavigate.Back)
        })
    }

    fun onCategoryChanged(category: String) {
        val model = categories.value?.first { it.name.equals(category, true) } ?: return
        _category.postValue(model)
        newRecipe.category = model
    }

    fun onDifficultyChanged(difficulty: String) {
        val model = difficulties.value?.first { it.name.equals(difficulty, true) } ?: return
        _difficulty.postValue(model)
        newRecipe.difficulty = model
    }

    fun onNameChanged(value: String) {
        newRecipe.name = value
    }

    fun onChangeCounter(isIncrement: Boolean) {
        when {
            isIncrement && newRecipe.servings < 11 -> {
                newRecipe.servings++
            }

            !isIncrement && newRecipe.servings > 1 -> {
                newRecipe.servings--
            }
        }
        dataChanged.value = true
    }

    fun onChangeTime(value: Int) {
        newRecipe.time = value
        dataChanged.value = true
    }

    fun getCookingTime(): Float {
        return newRecipe.time.toFloat()
    }

    fun onImageSelected(imageURI: Uri) {
        newRecipe.imagePath = imageURI.toString()
        dataChanged.value = true
    }

    fun setIngredients(values: List<IngredientModel>) {
        newRecipe.ingredients.apply {
            clear()
            addAll(values)
        }
    }

    fun setSteps(values: List<StepModel>) {
        newRecipe.steps.apply {
            clear()
            addAll(values)
        }
    }

    fun editRecipe() {
        if (checkRecipe()) {
            screenEvent.postValue(
                DialogModel(
                    title = R.string.dialog_edit_recipe_title,
                    message = R.string.dialog_edit_recipe_message,
                    positiveAction = {
                        handleResult({
                            recipeRepository.updateRecipe(newRecipe)
                        }, {
                            screenEvent.postValue(
                                MessageModel(message = "Successfully updated recipe")
                            )
                            actionNavigate.postValue(ActionNavigate.Back)
                        }, {
                            screenEvent.postValue(
                                SnackbarModel(message = "Error while updating recipe")
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
        return newRecipe.name.isNotBlank() && newRecipe.ingredients.isNotEmpty() && newRecipe.steps.isNotEmpty() && newRecipe.imagePath.isNotBlank()
    }
}