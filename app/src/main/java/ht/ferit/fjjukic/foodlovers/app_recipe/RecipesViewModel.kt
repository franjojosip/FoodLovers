package ht.ferit.fjjukic.foodlovers.app_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe

class RecipesViewModel : BaseViewModel() {

    private val _currentRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val currentRecipes: LiveData<List<HomeScreenRecipe>> = _currentRecipes

    fun loadRecipes(category: String?) {
        _currentRecipes.value = getRecipes()
    }

    private fun getRecipes(): List<HomeScreenRecipe> {
        return MockRepository.getRecipes().sortedBy { it.title }
    }

    fun handleSortBy(isAscending: Boolean) {
        when (isAscending) {
            true -> {
                _currentRecipes.value = currentRecipes.value?.sortedBy { it.title }
            }

            else -> {
                _currentRecipes.value = currentRecipes.value?.sortedByDescending { it.title }
            }
        }
    }
}