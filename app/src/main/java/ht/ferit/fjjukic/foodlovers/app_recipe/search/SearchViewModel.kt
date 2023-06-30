package ht.ferit.fjjukic.foodlovers.app_recipe.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.FilterRepositoryMock
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipes
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _selectedFilters = MutableLiveData<MutableList<FilterItem>>()
    val filters: LiveData<MutableList<FilterItem>>
        get() = _selectedFilters

    private val _searchHistory = MutableLiveData<MutableList<FilterItem>>()
    val searchHistory: LiveData<MutableList<FilterItem>>
        get() = _searchHistory

    val selectedCategories: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Category>() ?: listOf()

    val selectedTimes: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Time>() ?: listOf()

    val selectedDifficulties: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Difficulty>() ?: listOf()

    val selectedSorts: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Sort>() ?: listOf()

    private var recipes = listOf<HomeScreenRecipe>()

    private val _currentRecipes =
        MutableLiveData<List<HomeScreenRecipe>>(listOf(NoRecipePlaceholder))
    val currentRecipes: LiveData<List<HomeScreenRecipe>> = _currentRecipes

    private val _categories = MutableLiveData<List<RecipeCategory>>()
    val categories: LiveData<List<RecipeCategory>> = _categories

    fun init() {
        loadRecipes()
    }

    private fun loadRecipes() {
        handleResult(
            {
                recipeRepository.getRecipes()
            }, { data ->
                if (data != null) {
                    val recipes = if (data.isEmpty()) {
                        mutableListOf(NoRecipePlaceholder)
                    } else {
                        data.toMutableList().mapToBasicRecipes()
                    }

                    withContext(Dispatchers.Main) {
                        _currentRecipes.value = recipes
                    }
                }
            }, {}
        )
    }

    fun onConfirmFilterClicked(
        categories: List<FilterItem>,
        times: List<FilterItem>,
        difficulties: List<FilterItem>,
        sorts: List<FilterItem>,
    ) {
        val searchItems = _selectedFilters.value?.filterIsInstance<FilterItem.Search>() ?: listOf()

        _selectedFilters.value = mutableListOf<FilterItem>().apply {
            clear()
            addAll(searchItems)
            addAll(categories)
            addAll(times)
            addAll(difficulties)
            addAll(sorts)
        }

        _actionNavigate.postValue(ActionNavigate.Back)
    }

    fun onCategoryFilterSelected(category: String) {
        val currentFilters = _selectedFilters.value ?: mutableListOf()
        if (currentFilters.firstOrNull { it.value.equals(category, true) } == null) {
            currentFilters.add(FilterItem.Category(category, true))
            _selectedFilters.value = currentFilters

            //Filter recipe by category
        }
    }

    fun getFilteredRecipes(): MutableList<HomeScreenRecipe> {
        return mutableListOf(NoRecipePlaceholder)
    }

    fun getCategoryFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Category>()
            .onEach {
                if (_selectedFilters.value?.contains(it) == true) {
                    it.isChecked = true
                }
            }
    }

    fun getTimeFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Time>().onEach {
            if (_selectedFilters.value?.contains(it) == true) {
                it.isChecked = true
            }
        }
    }

    fun getDifficultyFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Difficulty>()
            .onEach {
                if (_selectedFilters.value?.contains(it) == true) {
                    it.isChecked = true
                }
            }
    }

    fun getSortFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Sort>().onEach {
            if (_selectedFilters.value?.contains(it) == true) {
                it.isChecked = true
            }
        }
    }

    fun removeFilter(item: FilterItem) {
        when (item) {
            is FilterItem.Search -> {
                val currentHistory = _searchHistory.value ?: mutableListOf()
                currentHistory.removeAll { it.value.equals(item.value, true) }

                _searchHistory.value = currentHistory
            }

            else -> {
                val currentFilters = _selectedFilters.value ?: mutableListOf()
                currentFilters.removeAll { it.value.equals(item.value, true) }

                _selectedFilters.value = currentFilters
            }
        }
    }

    fun addSearchFilter(value: String) {
        val searchHistory = _searchHistory.value ?: mutableListOf()
        if (searchHistory.none { it.value.equals(value, true) }) {
            searchHistory.add(FilterItem.Search(value))
            _searchHistory.value = searchHistory
            _currentRecipes.value =
                recipes.filter {
                    it.title.contains(value)
                            || it.description.contains(value)
                            || it.time.contains(value)
                            || it.difficulty.contains(value)
                }
        }
    }

    fun onRecipeClick(navDirections: NavDirections) {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(navDirections)
        )
    }

    fun onCategoryClick(category: String) {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                HomeFragmentDirections.actionNavHomeToNavSearchCategory(category)
            )
        )
    }

    fun onSearchClick() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                HomeFragmentDirections.actionNavHomeToNavSearchRecipes()
            )
        )
    }

    fun onFilterClick() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                SearchFragmentDirections.actionNavSearchRecipesToNavFilterRecipes()
            )
        )
    }
}