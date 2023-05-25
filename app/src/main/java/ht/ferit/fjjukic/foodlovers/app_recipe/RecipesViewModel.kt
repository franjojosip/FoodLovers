package ht.ferit.fjjukic.foodlovers.app_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NewRecipe

class RecipesViewModel : BaseViewModel() {

    private val _recipes = MutableLiveData<List<NewRecipe>>()
    val recipes: LiveData<List<NewRecipe>> = _recipes

    fun loadRecipes(category: String?) {
        if (category.isNullOrBlank()) {
            //WRITE MESSAGE AND BACK
        } else {
            //LOAD RECIPES FOR GIVEN CATEGORY
        }
    }

    fun handleSortBy(isAscending: Boolean) {
        when (isAscending) {
            true -> {
                _recipes.value = recipes.value?.sortedBy { it.name }
            }

            else -> {
                _recipes.value = recipes.value?.sortedByDescending { it.name }
            }
        }
    }
}