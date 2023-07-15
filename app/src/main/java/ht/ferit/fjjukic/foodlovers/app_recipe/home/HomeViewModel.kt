package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToRecipeCategory
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToTodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToTopRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val recipeRepository: RecipeRepository,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    private val _todayChoiceRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val todayChoiceRecipes: LiveData<List<HomeScreenRecipe>> = _todayChoiceRecipes

    private val _topRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val topRecipes: LiveData<List<HomeScreenRecipe>> = _topRecipes

    private val _categories = MutableLiveData<List<RecipeCategory>>()
    val categories: LiveData<List<RecipeCategory>> = _categories

    private var job: Job? = null

    init {
        loadCategories()
    }

    fun init() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            loadRecipes()
        }
    }

    private suspend fun loadRecipes() {
        handleResult(
            {
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
            }, {}
        )
    }

    private fun loadCategories() {
        handleResult({
            categoryRepository.getCategories()
        }, { categories ->
            val mappedCategories = categories?.mapToRecipeCategory()
            mappedCategories?.let {
                withContext(Dispatchers.Main) {
                    _categories.value = it
                }
            }
        }, {}
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

    override fun onCleared() {
        super.onCleared()
        job = null
    }
}