package ht.ferit.fjjukic.foodlovers.app_recipe.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.FilterModel
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.repository.filters.FilterRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val recipeRepository: RecipeRepository,
    private val filterRepository: FilterRepository
) : BaseViewModel() {

    private val _selectedFilters = MutableLiveData<List<FilterItem>>()
    val selectedFilters: LiveData<List<FilterItem>> = _selectedFilters

    private val _searchHistory = MutableLiveData<MutableList<FilterItem.Search>>()
    val searchHistory: LiveData<MutableList<FilterItem.Search>> = _searchHistory

    private val _filteredRecipes =
        MutableLiveData<List<HomeScreenRecipe>>(listOf(NoRecipePlaceholder))
    val filteredRecipes: LiveData<List<HomeScreenRecipe>> = _filteredRecipes

    var filter = FilterModel()
        private set

    private var recipes = listOf<HomeScreenRecipe>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filter = filterRepository.getFilterModel()


            recipes = recipeRepository.getRecipes().getOrDefault(listOf()).map {
                it.mapToBasicRecipe()
            }

            _filteredRecipes.postValue(recipes.ifEmpty { listOf(NoRecipePlaceholder) })
        }
    }

    fun onConfirmFilterClicked(
        selectedCategories: List<FilterItem>,
        selectedTimes: List<FilterItem>,
        selectedDifficulties: List<FilterItem>,
        selectedSorts: List<FilterItem>,
    ) {
        selectFilters(filter.categories, selectedCategories)
        selectFilters(filter.times, selectedTimes)
        selectFilters(filter.difficulties, selectedDifficulties)
        selectFilters(filter.sorts, selectedSorts)

        filterRecipes()

        _selectedFilters.value = filter.getSelectedFilters()

        actionNavigate.postValue(ActionNavigate.Back)
    }

    private fun filterRecipes() {
        viewModelScope.launch(Dispatchers.Default) {
            screenEvent.postValue(LoadingBar(true))

            val filters = filter.getSelectedFilters()
            val searchHistory = filter.searchHistory.map { it.value.lowercase() }

            val filteredRecipes = filters
                .map { it.value.lowercase() }
                .takeIf { it.isNotEmpty() }
                ?.let { filterValues ->
                    var filteredRecipes = recipes.filter {
                        filterValues.contains(it.title.lowercase())
                                || filterValues.contains(it.time.lowercase())
                                || filterValues.contains(it.difficulty.lowercase())
                                || filterValues.contains(it.category.lowercase())

                                || checkSearchHistory(it.title.lowercase(), searchHistory)
                                || checkSearchHistory(it.time.lowercase(), searchHistory)
                                || checkSearchHistory(it.difficulty.lowercase(), searchHistory)
                                || checkSearchHistory(it.category.lowercase(), searchHistory)
                    }

                    filter.sorts.firstOrNull()?.let {
                        filteredRecipes = if (it.isAsc) {
                            filteredRecipes.sortedBy { it.title }
                        } else {
                            filteredRecipes.sortedByDescending { it.title }
                        }
                    }
                    filteredRecipes
                }?.ifEmpty { listOf(NoRecipePlaceholder) } ?: recipes

            screenEvent.postValue(LoadingBar(false))
            withContext(Dispatchers.Main) {
                _selectedFilters.value = filters
                _filteredRecipes.value = filteredRecipes
            }
        }
    }

    private fun checkSearchHistory(value: String, history: List<String>): Boolean {
        var valueSearched = false
        history.forEach {
            if (value.contains(it)) valueSearched = true
        }
        return valueSearched
    }

    private fun selectFilters(filters: List<FilterItem>, selectedFilters: List<FilterItem>) {
        filters.forEach {
            it.isChecked = selectedFilters.contains(it)
        }
    }

    fun removeFilter(item: FilterItem) {
        filter.deselectFilter(item)

        if (item is FilterItem.Search) {
            _searchHistory.value = filter.searchHistory
        } else filterRecipes()
    }

    fun addSearchFilter(value: String) {
        val searchHistory = filter.searchHistory.toMutableList()

        if (searchHistory.none { it.value.equals(value, true) }) {
            searchHistory.add(FilterItem.Search(value))
        }

        filter.searchHistory.apply {
            clear()
            addAll(searchHistory)
        }
        _searchHistory.value = filter.searchHistory

        _filteredRecipes.value = _filteredRecipes.value?.filter {
            it.title.lowercase().contains(value.lowercase()) ||
                    it.time.lowercase().contains(value.lowercase()) ||
                    it.difficulty.lowercase().contains(value.lowercase()) ||
                    it.category.lowercase().contains(value.lowercase())
        }?.ifEmpty { listOf(NoRecipePlaceholder) } ?: listOf(NoRecipePlaceholder)
    }

    fun removeSearchFilter() {
        filterRecipes()
    }

    fun onRecipeClick(id: String) {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                SearchFragmentDirections.actionNavSearchRecipesToNavGraphShowRecipe(id)
            )
        )
    }

    fun onFilterClick() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                SearchFragmentDirections.actionNavSearchRecipesToNavFilterRecipes()
            )
        )
    }
}