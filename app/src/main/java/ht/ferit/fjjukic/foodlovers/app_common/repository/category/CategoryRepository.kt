package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel

interface CategoryRepository {
    suspend fun getCategory(id: String): Result<CategoryModel?>
    suspend fun getCategories(): Result<List<CategoryModel>>
    suspend fun updateCategory(category: CategoryModel): Result<Boolean>
    suspend fun createCategory(category: CategoryModel): Result<Boolean>
    suspend fun deleteCategory(id: String): Result<Boolean>
}