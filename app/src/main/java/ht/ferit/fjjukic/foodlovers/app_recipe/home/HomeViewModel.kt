package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.FilterRepositoryMock
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import ht.ferit.fjjukic.foodlovers.app_recipe.search.SearchFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
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

    private val _todayChoiceRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val todayChoiceRecipes: LiveData<List<HomeScreenRecipe>> = _todayChoiceRecipes

    private val _topRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val topRecipes: LiveData<List<HomeScreenRecipe>> = _topRecipes

    private var recipes = listOf<HomeScreenRecipe>()

    private val _currentRecipes = MutableLiveData<List<HomeScreenRecipe>>()
    val currentRecipes: LiveData<List<HomeScreenRecipe>> = _currentRecipes

    val categories = MediatorLiveData<List<RecipeCategory>>()

    init {
        categories.addSource(categoryRepository.getCategories()) {
            viewModelScope.launch(Dispatchers.Default) {
                categories.postValue(
                    it.mapToRecipeCategory().sortedBy { it.title }
                )
            }
        }

        recipes = MockRepository.getRecipes()

        _todayChoiceRecipes.value = MockRepository.getTodayChoiceRecipes()
        _topRecipes.value = MockRepository.getTopRecipes()
        _currentRecipes.value = recipes

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

    private fun List<Category>.mapToRecipeCategory(): List<RecipeCategory> {
        return map { category ->
            RecipeCategory(
                category.id,
                category.name,
                category.drawableId
            )
        }
    }
}