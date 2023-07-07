package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToRecipeCategory
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToTodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToTopRecipe
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _todayChoiceRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val todayChoiceRecipes: LiveData<List<HomeScreenRecipe>> = _todayChoiceRecipes

    private val _topRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val topRecipes: LiveData<List<HomeScreenRecipe>> = _topRecipes

    private val _categories = MutableLiveData<List<RecipeCategory>>()
    val categories: LiveData<List<RecipeCategory>> = _categories

    fun init() {
        loadCategories()
        loadRecipes()
    }

    private fun loadRecipes() {
        handleResult({
            recipeRepository.getRecipes()
        }, { data ->
            data?.let {
                val topRecipes = data.takeLast(3).mapToTopRecipe()
                val todayChoiceRecipes = data.take(5).mapToTodayChoiceRecipe()

                withContext(Dispatchers.Main) {
                    _todayChoiceRecipes.value = todayChoiceRecipes
                    _topRecipes.value = topRecipes
                }
            }
        }, {},
            showLoading = true
        )
    }

    private fun loadCategories() {
        handleResult({
            categoryRepository.getCategories()
        }, { categories ->
            categories?.let {
                withContext(Dispatchers.Main) {
                    _categories.value = it.mapToRecipeCategory()
                }
            }
        }, {},
            showLoading = false
        )
    }

    fun onRecipeClick(navDirections: NavDirections) {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(navDirections)
        )
    }

    fun onCategoryClick(category: String) {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                HomeFragmentDirections.actionNavHomeToNavSearchCategory(category)
            )
        )
    }

    fun onSearchClick() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                HomeFragmentDirections.actionNavHomeToNavSearchRecipes()
            )
        )
    }
}