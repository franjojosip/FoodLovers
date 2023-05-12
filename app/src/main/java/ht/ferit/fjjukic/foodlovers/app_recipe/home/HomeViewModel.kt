package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.FilterRepositoryMock
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Category
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder

class HomeViewModel(private val categoryRepository: CategoryRepository) : BaseViewModel() {
    private val _selectedFilters = MutableLiveData<MutableList<FilterItem>>()

    val filters: LiveData<MutableList<FilterItem>>
        get() = _selectedFilters

    val selectedCategories: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Category>() ?: listOf()

    val selectedTimes: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Time>() ?: listOf()

    val selectedDifficulties: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Difficulty>() ?: listOf()

    val selectedSorts: List<FilterItem>
        get() = _selectedFilters.value?.filterIsInstance<FilterItem.Sort>() ?: listOf()

    val actionNavigate: LiveData<ActionNavigate> = _actionNavigate

    fun getCategories(): List<Category> {
        return categoryRepository.getCategories()
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
        val searchItems = _selectedFilters.value?.filterIsInstance<FilterItem.Search>() ?: listOf()

        _selectedFilters.value =  mutableListOf<FilterItem>().apply {
            clear()
            addAll(searchItems)
            addAll(categories)
            addAll(times)
            addAll(difficulties)
            addAll(sorts)
        }
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
        val currentFilters = _selectedFilters.value ?: mutableListOf()
        currentFilters.removeAll { it.value.equals(item.value, true) }

        _selectedFilters.value = currentFilters
    }

    fun addSearchFilter(value: String) {
        val currentFilters = _selectedFilters.value ?: mutableListOf()
        if (currentFilters.none { it.value.equals(value, true) }) {
            currentFilters.add(FilterItem.Search(value))
            _selectedFilters.value = currentFilters
        }
    }

    fun onRecipeClicked(id: String) {
        _actionNavigate.postValue(ActionNavigate.ShowRecipe(id))
    }

    fun onCategoryClicked(category: String) {
        _actionNavigate.postValue(ActionNavigate.CategoryRecipes(category))
    }
}