package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
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
    private val difficultyRepository: DifficultyRepository,
    analyticsProvider: AnalyticsProvider,
    private val recipeId: String?
) : BaseViewModel(analyticsProvider) {
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

    val isEditMode by lazy { recipeId != null }

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadCategories()
                loadDifficulties()
                loadRecipe()
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
        }
    }

    private suspend fun loadCategories() {
        handleResult({
            categoryRepository.getCategories()
        }, {
            withContext(Dispatchers.Main) {
                _categories.value = it ?: listOf()
            }
        }, {
            showSnackbar(it?.message)
        })
    }

    private suspend fun loadDifficulties() {
        handleResult({
            difficultyRepository.getDifficulties()
        }, {
            withContext(Dispatchers.Main) {
                _difficulties.value = it ?: listOf()
            }
        }, {
            showSnackbar(it?.message)
        })
    }

    private suspend fun loadRecipe() {
        handleResult({
            when {
                recipeId != null -> recipeRepository.getRecipe(recipeId)
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
                            category = categories.value?.first()
                            difficulty = difficulties.value?.first()
                        }
                        _oldRecipe.value = model
                        recipe = model
                    }
                }
            }
        }, {
            showSnackbar(it?.message)
            actionNavigate.postValue(ActionNavigate.Back)
        })
    }

    fun onCategoryChanged(category: String) {
        val model = categories.value?.first { it.name.equals(category, true) } ?: return
        _category.postValue(model)
        recipe.category = model
        dataChanged.value = true
    }

    fun onDifficultyChanged(difficulty: String) {
        val model = difficulties.value?.first { it.name.equals(difficulty, true) } ?: return
        _difficulty.postValue(model)
        recipe.difficulty = model
        dataChanged.value = true
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