package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category

class CategoryRepositoryImpl(
    private val db: RecipeDatabase
) : CategoryRepository {
    override fun getCategories(): LiveData<List<Category>> {
        return db.categoryDao().getAll()
    }
}