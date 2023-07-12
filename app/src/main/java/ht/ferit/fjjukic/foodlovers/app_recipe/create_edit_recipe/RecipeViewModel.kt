package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe

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

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository
) : BaseViewModel() {
    companion object {
        const val NUM_OF_STEPS = 4

        const val MAX_SERVINGS = 11
        const val MIN_SERVINGS = 1
    }

    private val _oldRecipe: MutableLiveData<RecipeModel> = MutableLiveData()
    val oldRecipe: LiveData<RecipeModel> = _oldRecipe

    var recipe = RecipeModel()
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

    private var recipeId: String? = null
    val isEditMode by lazy { recipeId != null }

    fun init(id: String?) {
        recipeId = id

        viewModelScope.launch(Dispatchers.IO) {
            val categories = categoryRepository.getCategories().getOrDefault(listOf())
            val difficulties = difficultyRepository.getDifficulties().getOrDefault(listOf())
            _categories.postValue(categories)
            _difficulties.postValue(difficulties)
            loadRecipe(categories.first(), difficulties.first())
        }

    }

    private suspend fun loadRecipe(
        defaultCategory: CategoryModel,
        defaultDifficulty: DifficultyModel
    ) {
        handleResult({
            when {
                recipeId != null -> recipeRepository.getRecipe(recipeId!!)
                else -> Result.success(null)
            }
        }, {
            when {
                recipeId != null && it == null -> {
                    actionNavigate.postValue(ActionNavigate.Back)
                }

                it != null -> {
                    withContext(Dispatchers.Main) {
                        _oldRecipe.value = it
                        recipe = it
                    }
                }

                else -> {
                    withContext(Dispatchers.Main) {
                        val model = RecipeModel().apply {
                            ingredients = mutableListOf(IngredientModel(), IngredientModel())
                            steps = mutableListOf(StepModel())
                            category = defaultCategory
                            difficulty = defaultDifficulty
                        }
                        _oldRecipe.value = model
                        recipe = model
                    }
                }
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
        recipe.category = model
    }

    fun onDifficultyChanged(difficulty: String) {
        val model = difficulties.value?.first { it.name.equals(difficulty, true) } ?: return
        _difficulty.postValue(model)
        recipe.difficulty = model
    }

    fun onNameChanged(value: String) {
        recipe.name = value
    }

    fun onChangeCounter(isIncrement: Boolean) {
        when {
            isIncrement && recipe.servings < 11 -> {
                recipe.servings++
            }

            !isIncrement && recipe.servings > 1 -> {
                recipe.servings--
            }
        }
        dataChanged.value = true
    }

    fun onChangeTime(value: Int) {
        recipe.time = value
        dataChanged.value = true
    }

    fun onImageSelected(imageURI: Uri) {
        recipe.imagePath = imageURI.toString()
        dataChanged.value = true
    }

    fun setIngredients(values: List<IngredientModel>) {
        recipe.ingredients.apply {
            clear()
            addAll(values)
        }
        dataChanged.value = true
    }

    fun setSteps(values: List<StepModel>) {
        recipe.steps.apply {
            clear()
            addAll(values)
        }
        dataChanged.value = true
    }

    fun onRecipeAction() {
        if (checkRecipe()) {
            screenEvent.postValue(
                DialogModel(
                    title = if (isEditMode) R.string.dialog_edit_recipe_title else R.string.dialog_create_recipe_title,
                    message = if (isEditMode) R.string.dialog_edit_recipe_message else R.string.dialog_create_recipe_message,
                    positiveAction = {
                        handleResult({
                            if (isEditMode) {
                                recipeRepository.updateRecipe(recipe)
                            } else {
                                recipeRepository.createRecipe(recipe)
                            }
                        }, {
                            messageScreenEvent.postValue(
                                MessageModel(
                                    messageId = if (isEditMode) R.string.message_recipe_updated
                                    else R.string.message_recipe_created
                                )
                            )
                            actionNavigate.postValue(ActionNavigate.Back)
                        }, {
                            messageScreenEvent.postValue(
                                MessageModel(
                                    messageId = if (isEditMode) R.string.message_recipe_not_updated
                                    else R.string.message_recipe_not_created
                                )
                            )
                        })
                    },
                    positiveTitleId = R.string.dialog_btn_confirm
                )
            )
        } else {
            screenEvent.postValue(
                SnackbarModel(message = "Please check all recipe fields!")
            )
        }
    }

    private fun checkRecipe(): Boolean {
        return recipe.name.isNotBlank() &&
                recipe.ingredients.isNotEmpty() &&
                checkList(recipe.ingredients) &&
                recipe.steps.isNotEmpty() &&
                checkList(recipe.steps) &&
                recipe.imagePath.isNotBlank()
    }

    private fun <T> checkList(data: List<T>): Boolean {
        data.forEach {
            if ((it is StepModel && it.description.isBlank()) ||
                (it is IngredientModel && (it.name.isBlank() || it.amount.isBlank()))
            ) {
                return false
            }
        }
        return true
    }
}