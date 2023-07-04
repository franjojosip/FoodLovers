package ht.ferit.fjjukic.foodlovers.app_recipe.model

sealed class FilterItem(val value: String, var isChecked: Boolean, val isDefault: Boolean) {

    class Search(value: String) : FilterItem(value, true, false)

    class Category(value: String, isChecked: Boolean = false, isDefault: Boolean = false) :
        FilterItem(value, isChecked, isDefault)

    class Time(
        value: String,
        val time: Int,
        isChecked: Boolean = false,
        isDefault: Boolean = false
    ) :
        FilterItem(value, isChecked, isDefault)

    class Difficulty(value: String, isChecked: Boolean = false, isDefault: Boolean = false) :
        FilterItem(value, isChecked, isDefault)

    class Sort(
        value: String,
        val isAsc: Boolean = false,
        isChecked: Boolean = false,
        isDefault: Boolean = false
    ) :
        FilterItem(value, isChecked, isDefault)
}
