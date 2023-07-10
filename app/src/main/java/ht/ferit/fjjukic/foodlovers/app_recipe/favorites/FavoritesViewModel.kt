package ht.ferit.fjjukic.foodlovers.app_recipe.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipes
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {
    private var recipes: List<HomeScreenRecipe> = listOf(NoRecipePlaceholder)

    private val _filteredRecipes =
        MutableLiveData<List<HomeScreenRecipe>>(listOf(NoRecipePlaceholder))
    val filteredRecipes: LiveData<List<HomeScreenRecipe>> = _filteredRecipes

    private var searchFilter: String? = null
    var isAscending = true
        private set

    fun loadRecipes() {
        handleResult({
            recipeRepository.getRecipes()
        }, {
            viewModelScope.launch(Dispatchers.Default) {
                val mappedRecipes =
                    it?.filter { it.isFavorite }?.mapToBasicRecipes() ?: listOf(NoRecipePlaceholder)

                recipes = mappedRecipes

                searchFilter?.let {
                    filterBySearch(it)
                } ?: run {
                    sortData(mappedRecipes)
                }
            }
        }, {})
    }

    fun onSortByClick() {
        isAscending = !isAscending
        sortData(filteredRecipes.value ?: listOf())
    }

    private fun sortData(data: List<HomeScreenRecipe>) {
        viewModelScope.launch(Dispatchers.Default) {
            val sortedData = when (isAscending) {
                true -> {
                    data.sortedBy { it.title }
                }

                else -> {
                    data.sortedByDescending { it.title }
                }
            }
            withContext(Dispatchers.Main) {
                _filteredRecipes.value = sortedData
            }
        }
    }

    fun filterBySearch(value: String) {
        viewModelScope.launch(Dispatchers.Default) {
            searchFilter = value

            val filteredRecipes = recipes.filter {
                it.title.lowercase().contains(value.lowercase()) ||
                        it.time.lowercase().contains(value.lowercase()) ||
                        it.difficulty.lowercase().contains(value.lowercase()) ||
                        it.category.lowercase().contains(value.lowercase()) ||
                        it.user.lowercase().contains(value.lowercase())
            }.ifEmpty { listOf(NoRecipePlaceholder) }

            sortData(filteredRecipes)
        }
    }

    fun removeSearchFilter() {
        searchFilter = null
        sortData(recipes)
    }

    fun onRecipeClick(id: String) {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                FavoritesFragmentDirections.actionNavGraphFavoritesToNavGraphShowRecipe(id)
            )
        )
    }
}