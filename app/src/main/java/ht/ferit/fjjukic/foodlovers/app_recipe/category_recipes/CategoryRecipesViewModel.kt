package ht.ferit.fjjukic.foodlovers.app_recipe.category_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipes
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryRecipesViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private var recipes: List<HomeScreenRecipe> = listOf(NoRecipePlaceholder)

    private val _filteredRecipes =
        MutableLiveData<List<HomeScreenRecipe>>()
    val filteredRecipes: LiveData<List<HomeScreenRecipe>> = _filteredRecipes

    private var searchFilter: String? = null
    var isAscending = true
        private set

    fun loadRecipes(category: String? = null) {
        handleResult({
            recipeRepository.getRecipes()
        }, {
            viewModelScope.launch(Dispatchers.Default) {
                var mappedRecipes = it?.mapToBasicRecipes()

                if (!category.isNullOrBlank()) {
                    mappedRecipes = mappedRecipes?.filter { model ->
                        model.category.contains(category, true)
                    }?.ifEmpty { listOf(NoRecipePlaceholder) } ?: listOf(NoRecipePlaceholder)
                }

                recipes = mappedRecipes ?: listOf(NoRecipePlaceholder)

                searchFilter?.let {
                    filterBySearch(it)
                } ?: run {
                    sortData(recipes)
                }
            }
        }, {})
    }

    private fun sortData(data: List<HomeScreenRecipe>) {
        viewModelScope.launch(Dispatchers.Default) {
            val sortedData = when (isAscending) {
                true -> {
                    data.sortedBy { it.title.lowercase() }
                }

                else -> {
                    data.sortedByDescending { it.title.lowercase() }
                }
            }
            withContext(Dispatchers.Main) {
                _filteredRecipes.value = sortedData
            }
        }
    }

    fun onSortByClick() {
        isAscending = !isAscending
        sortData(filteredRecipes.value ?: listOf())
    }

    fun filterBySearch(value: String) {
        viewModelScope.launch(Dispatchers.Default) {
            searchFilter = value

            val filteredRecipes = recipes.filter {
                it.title.lowercase().contains(value.lowercase()) ||
                        it.time.lowercase().contains(value.lowercase()) ||
                        it.difficulty.lowercase().contains(value.lowercase()) ||
                        it.user.lowercase().contains(value.lowercase())
            }.ifEmpty { listOf(NoRecipePlaceholder) }

            sortData(filteredRecipes)
        }
    }

    fun removeSearchFilter() {
        searchFilter = null
        sortData(recipes)
    }

    fun onRecipeClick(navDirections: NavDirections) {
        actionNavigate.postValue(ActionNavigate.NavigationWithDirections(navDirections))
    }
}