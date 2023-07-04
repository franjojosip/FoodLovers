package ht.ferit.fjjukic.foodlovers.app_recipe.search

import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem

data class SearchModel(
    val selectedCategories: MutableList<FilterItem.Category> = mutableListOf(),
    val selectedTimes: MutableList<FilterItem.Time> = mutableListOf(),
    val selectedDifficulties: MutableList<FilterItem.Difficulty> = mutableListOf(),
    val selectedSorts: MutableList<FilterItem.Sort> = mutableListOf()
)