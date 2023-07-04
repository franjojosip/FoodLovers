package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class CategoryRepositoryImpl(
    private val db: RecipeDatabase,
    private val firebaseDB: FirebaseDB,
    private val preferenceManager: PreferenceManager
) : CategoryRepository {

    override suspend fun getCategories(): Result<List<CategoryModel>> {
        return withContext(Dispatchers.IO) {
            val categories = db.categoryDao().getAll()

            when {
                hasDayPassed() -> {
                    val newCategories = firebaseDB.getCategories().getOrDefault(listOf())

                    if (newCategories.isNotEmpty()) {
                        saveCategories(newCategories)
                        Result.success(newCategories)
                    } else {
                        Result.success(mapToCategoryModels(categories))
                    }
                }

                categories.isNotEmpty() -> {
                    Result.success(mapToCategoryModels(categories))
                }

                else -> {
                    val newCategories = firebaseDB.getCategories().getOrDefault(listOf())
                    saveCategories(newCategories)

                    Result.success(newCategories)
                }
            }
        }
    }

    private fun hasDayPassed(): Boolean {
        return (System.currentTimeMillis() - preferenceManager.lastUpdatedCategories) >= TimeUnit.DAYS.toMillis(
            1
        )
    }

    private fun saveCategories(data: List<CategoryModel>) {
        preferenceManager.lastUpdatedCategories = System.currentTimeMillis()
        data.forEach {
            db.categoryDao().insert(it.mapToCategory())
        }
    }

    override suspend fun updateCategory(category: CategoryModel): Result<CategoryModel> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.updateCategory(category)

            if (result.isSuccess) {
                saveCategory(category)
                result
            } else {
                result
            }
        }
    }

    override suspend fun createCategory(category: CategoryModel): Result<CategoryModel> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.createCategory(category)

            if (result.isSuccess) {
                saveCategory(category)
                result
            } else {
                result
            }
        }
    }

    override suspend fun deleteCategory(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            db.categoryDao().delete(id)
            firebaseDB.deleteCategory(id)
        }
    }

    override suspend fun getFilterCategories(): List<FilterItem.Category> {
        return getCategories().getOrDefault(listOf()).let { models ->
            val categories =
                mutableListOf(FilterItem.Category("All", isChecked = true, isDefault = true))
            categories.addAll(models.map { FilterItem.Category(it.name.replaceFirstChar(Char::titlecase)) })
            categories.sortedBy { it.value }
        }
    }

    override suspend fun getCategory(id: String): Result<CategoryModel?> {
        return withContext(Dispatchers.IO) {
            val category = db.categoryDao().get(id)?.mapToCategoryModel()

            if (category != null) {
                Result.success(category)
            } else {
                val firebaseCategory = firebaseDB.getCategory(id).getOrNull()

                if (firebaseCategory != null) {
                    saveCategory(firebaseCategory)
                    Result.success(firebaseCategory)
                } else {
                    Result.failure(Exception("Error while retrieving data"))
                }
            }
        }
    }

    private fun saveCategory(category: CategoryModel) {
        db.categoryDao().insert(category.mapToCategory())
    }

    private fun mapToCategoryModels(data: List<Category>): List<CategoryModel> {
        val categories = mutableListOf<CategoryModel>()

        data.forEach {
            categories.add(it.mapToCategoryModel())
        }
        return categories
    }

    private fun Category.mapToCategoryModel(): CategoryModel {
        return CategoryModel(id, name, drawableId)
    }

    private fun CategoryModel.mapToCategory(): Category {
        return Category(id, name, drawableId)
    }
}