package ht.ferit.fjjukic.foodlovers.app_common.repository

import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem

class FilterRepositoryMock {
    fun getFilterItems(): List<FilterItem> {

        val categories: MutableList<FilterItem> = listOf(
            "All",
            "Breakfast",
            "Lunch",
            "Desert",
            "Dinner",
            "Soup",
            "Salad"
        ).mapIndexed { index, value -> FilterItem.Category(value, index == 0, index == 0) }
            .toMutableList()
        val times =
            listOf(
                "All",
                "15min",
                "30min",
                "45min",
                "1h"
            ).mapIndexed { index, value -> FilterItem.Time(value, index == 0, index == 0) }
                .toMutableList()
        val difficulties =
            listOf(
                "All",
                "Easy",
                "Medium",
                "Hard"
            ).mapIndexed { index, value -> FilterItem.Difficulty(value, index == 0, index == 0) }
                .toMutableList()
        val sorts = listOf("A-Z", "Z-A").mapIndexed { index, value ->
            FilterItem.Sort(
                value,
                isChecked = index == 0,
                isDefault = index == 0
            )
        }.toMutableList()

        return categories.apply {
            addAll(times)
            addAll(difficulties)
            addAll(sorts)
        }
    }
}