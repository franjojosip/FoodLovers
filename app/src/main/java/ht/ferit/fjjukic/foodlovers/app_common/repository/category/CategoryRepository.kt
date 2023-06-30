package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel

interface CategoryRepository {
    suspend fun getCategory(id: String): Result<CategoryModel?>
    suspend fun getCategories(): Result<List<CategoryModel>>
    suspend fun createCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun updateCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun deleteCategory(id: String): Result<Boolean>
}