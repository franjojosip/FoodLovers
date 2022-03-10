package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.IngredientUI
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NewRecipe

class CreateRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val resourceRepository: ResourceRepository
) : BaseViewModel() {

    private var recipe = NewRecipe()

    private val _numOfServings = MutableLiveData("1")
    val numOfServings: LiveData<String> = _numOfServings

    private val _cookingTime = MutableLiveData("15 min")
    val cookingTime: LiveData<String> = _cookingTime

    private val _imagePath = MutableLiveData<Uri>()
    val imagePath: LiveData<Uri> = _imagePath

    val numOfSteps = 4

    private lateinit var _ingredients: MutableLiveData<List<IngredientUI>>

    fun onChangeCounter(isIncrement: Boolean) {
        when {
            isIncrement && recipe.servings < 11 -> {
                recipe.servings++
                _numOfServings.value = recipe.servings.toString()
            }
            recipe.servings > 1 -> {
                recipe.servings--
                _numOfServings.value = recipe.servings.toString()
            }
        }
    }

    fun onChangeTime(value: Int) {
        recipe.time = value
        _cookingTime.value = "${recipe.time} min"
    }

    fun getCookingTime(): Float {
        return recipe.time.toFloat()
    }

    fun onImageSelected(imageURI: Uri) {
        recipe.imageURI = imageURI.toString()
        _imagePath.value = imageURI
    }

}