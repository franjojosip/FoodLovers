package ht.ferit.fjjukic.foodlovers.app_recipe.home

import ht.ferit.fjjukic.foodlovers.app_common.repository.FilterRepositoryMock
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
import java.time.LocalDateTime

class HomeViewModel : BaseViewModel() {
    private val _selectedFilters = mutableListOf<FilterItem>()

    val filters: List<FilterItem>
        get() = _selectedFilters

    val selectedCategories: List<FilterItem>
        get() = _selectedFilters.filterIsInstance<FilterItem.Category>()

    val selectedTimes: List<FilterItem>
        get() = _selectedFilters.filterIsInstance<FilterItem.Time>()

    val selectedDifficulties: List<FilterItem>
        get() = _selectedFilters.filterIsInstance<FilterItem.Difficulty>()

    val selectedSorts: List<FilterItem>
        get() = _selectedFilters.filterIsInstance<FilterItem.Sort>()

    fun getCategories(): MutableList<HomeScreenRecipe> {
        return MockRepository.getCategories()
    }

    fun getTodayChoiceRecipes(): MutableList<HomeScreenRecipe> {
        return MockRepository.getTodayChoiceRecipes()
    }

    fun getTopRecipes(): MutableList<HomeScreenRecipe> {
        return MockRepository.getTopRecipes()
    }

    fun onConfirmFilterClicked(
        categories: List<FilterItem>,
        times: List<FilterItem>,
        difficulties: List<FilterItem>,
        sorts: List<FilterItem>,
    ) {
        val searchItems = _selectedFilters.filterIsInstance<FilterItem.Search>()

        _selectedFilters.apply {
            clear()
            addAll(searchItems)
            addAll(categories)
            addAll(times)
            addAll(difficulties)
            addAll(sorts)
        }
    }

    fun getFilteredRecipes(): MutableList<HomeScreenRecipe> {
        return mutableListOf(NoRecipePlaceholder)
    }

    fun getCategoryFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Category>()
            .onEach {
                if (_selectedFilters.contains(it)) {
                    it.isChecked = true
                }
            }
    }

    fun getTimeFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Time>().onEach {
            if (_selectedFilters.contains(it)) {
                it.isChecked = true
            }
        }
    }

    fun getDifficultyFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Difficulty>()
            .onEach {
                if (_selectedFilters.contains(it)) {
                    it.isChecked = true
                }
            }
    }

    fun getSortFilters(): List<FilterItem> {
        return FilterRepositoryMock().getFilterItems().filterIsInstance<FilterItem.Sort>().onEach {
            if (_selectedFilters.contains(it)) {
                it.isChecked = true
            }
        }
    }

    fun removeFilter(item: FilterItem) {
        _selectedFilters.removeAll { it.value.equals(item.value, true) }
    }

    fun addSearchFilter(item: FilterItem) {
        if (_selectedFilters.none { it.value.equals(item.value, true) }){
            _selectedFilters.add(item)
        }
    }
}