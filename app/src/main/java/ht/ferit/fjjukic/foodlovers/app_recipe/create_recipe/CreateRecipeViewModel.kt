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
    val author = "By Sara Tane" // TEST PURPOSE ONLY

    //    private val _recipe = NewRecipe()
    //TODO REMOVE AFTER MOCK
    private val _recipe = Recipe(
        name = "Tomato Basil Pasta",
        time = 30,
        servings = 4,
        author = "Random ",
        imagePath = "content://com.android.providers.media.documents/document/image%3A1000006904",
        ingredients = mutableListOf(
            Ingredient(
                name = "Tomatoes",
                amount = "5"
            ),
            Ingredient(
                name = "Garlic",
                amount = "1"
            ),
            Ingredient(
                name = "Parmesan",
                amount = "3/4 cup"
            ),
            Ingredient(
                name = "Butter",
                amount = "4 tablespoons"
            ),
            Ingredient(
                name = "Virgin Oil",
                amount = "3 tablespoons"
            ),
            Ingredient(
                name = "Lemon",
                amount = "1"
            ),
            Ingredient(
                name = "Honey",
                amount = "1 teaspoon"
            ),
            Ingredient(
                name = "Balsamic Vinegar",
                amount = "1 teaspoon"
            ),
            Ingredient(
                name = "Pasta",
                amount = "400g"
            ),
            Ingredient(
                name = "Basil leaves",
                amount = "1 1/2 cups"
            ),
        ),
        steps = mutableListOf(
            Step(
                position = 1,
                description = "Gently squeeze each tomato half to release any watery guts and seeds into a bowl and discard. Chop the tomato flesh into 1-inch pieces and add them into a large bowl. Mash with the back of a wooden spoon or potato masher, or use your hands to break down the tomatoes.\nEverything should be smaller than a nickel."
            ),
            Step(
                position = 2,
                description = "Add the garlic, Parmesan, butter, olive oil, lemon juice and zest, balsamic vinegar, honey, red pepper flakes, salt, and black pepper. Toss to combine. \nCover the bowl with plastic wrap and let sit at room temperature for 15 minutes."
            ),
            Step(
                position = 3,
                description = "Bring a large pot of heavily salted water to a boil. Cook the pasta until al dente. Use tongs or a spider to transfer the cooked pasta directly into the bowl with tomatoes and toss. It’s okay if some of the pasta water is added—this will help melt the butter and cheese. \nTaste the pasta and season with more salt and black pepper, if needed. Gently mix in the basil.."
            ),
            Step(
                position = 4,
                description = "Transfer the pasta into bowls, spooning any residual sauce over it. Garnish with more Parmesan and drizzle with extra virgin olive oil. Serve immediately.\nThe tomato sauce can be made up to 8 hours ahead. Keep it covered in the refrigerator until 1 hour before you plan to serve it."
            )
        )
    )

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