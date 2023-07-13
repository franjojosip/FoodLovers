package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem

interface CategoryRepository {
    fun init()
    suspend fun getCategory(id: String): Result<CategoryModel?>
    suspend fun getFilterCategories(): List<FilterItem.Category>
    suspend fun getCategories(): Result<List<CategoryModel>>
    suspend fun createCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun updateCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun deleteCategory(id: String): Result<Boolean>
}