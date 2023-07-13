package ht.ferit.fjjukic.foodlovers.app_common.model

import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem

data class FilterModel(
    val categories: List<FilterItem.Category> = mutableListOf(),
    val difficulties: List<FilterItem.Difficulty> = mutableListOf(),
    val times: List<FilterItem.Time> = mutableListOf(),
    val sorts: List<FilterItem.Sort> = mutableListOf(),
    val searchHistory: MutableList<FilterItem.Search> = mutableListOf()
) {
    fun getSelectedFilters(): List<FilterItem> {
        return mutableListOf<FilterItem>().apply {
            addAll(categories.filter { it.isChecked && !it.isDefault })
            addAll(difficulties.filter { it.isChecked && !it.isDefault })
            addAll(times.filter { it.isChecked && !it.isDefault })
            addAll(sorts.filter { it.isChecked && !it.isDefault })
        }
    }

    fun deselectFilter(item: FilterItem) {
        val filters = when (item) {
            is FilterItem.Category -> categories
            is FilterItem.Difficulty -> difficulties
            is FilterItem.Time -> times
            is FilterItem.Sort -> sorts
            is FilterItem.Search -> searchHistory
        }

        if (item is FilterItem.Search) {
            searchHistory.indexOfFirst { it.value.equals(item.value, false) }
                .takeIf { it != -1 }
                ?.let { index ->
                    searchHistory.removeAt(index)
                }
        } else {
            filters.firstOrNull { it.value == item.value }?.isChecked = false

            if (filters.count { it.isChecked } == 0) {
                filters.first { it.isDefault }.isChecked = true
            }
        }
    }
}