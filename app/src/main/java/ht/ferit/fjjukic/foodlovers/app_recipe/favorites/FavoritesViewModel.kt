package ht.ferit.fjjukic.foodlovers.app_recipe.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipe
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private var recipes: List<HomeScreenRecipe> = listOf(NoRecipePlaceholder)

    private val _filteredRecipes =
        MutableLiveData<List<HomeScreenRecipe>>(listOf(NoRecipePlaceholder))
    val filteredRecipes: LiveData<List<HomeScreenRecipe>> = _filteredRecipes

    fun loadRecipes(category: String? = null) {
        handleResult({
            recipeRepository.getRecipes()
        }, {
            it
                ?.filter { model ->
                    if (!category.isNullOrBlank()) {
                        model.category?.name?.contains(category, true) == true
                    } else true
                }
                ?.map { recipe -> recipe.mapToBasicRecipe() }
                ?.let {
                    withContext(Dispatchers.Main) {
                        recipes = it
                        _filteredRecipes.value = it.ifEmpty { listOf(NoRecipePlaceholder) }
                    }
                }
        }, {}
        )
    }

    fun handleSortBy(isAscending: Boolean) {
        when (isAscending) {
            true -> {
                _filteredRecipes.value = _filteredRecipes.value?.sortedBy { it.title }
            }

            else -> {
                _filteredRecipes.value = _filteredRecipes.value?.sortedByDescending { it.title }
            }
        }
    }

    fun addSearchFilter(value: String) {
        _filteredRecipes.value = recipes.filter {
            it.title.lowercase().contains(value.lowercase()) ||
                    it.time.lowercase().contains(value.lowercase())
        }
    }

    fun removeSearchFilter(isAscending: Boolean) {
        _filteredRecipes.value = recipes.let { list ->
            when (isAscending) {
                true -> {
                    list.sortedBy { it.title }
                }

                else -> {
                    list.sortedByDescending { it.title }
                }
            }
        }
    }

    fun onRecipeClick(id: String) {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                FavoritesFragmentDirections.actionNavGraphFavoritesToNavGraphShowRecipe(id)
            )
        )
    }
}