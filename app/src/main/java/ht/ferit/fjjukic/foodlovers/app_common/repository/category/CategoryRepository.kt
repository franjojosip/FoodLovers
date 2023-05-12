package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import ht.ferit.fjjukic.foodlovers.app_recipe.model.Category

interface CategoryRepository {
    fun getCategories(): List<Category>
}