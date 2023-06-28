package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category

interface CategoryRepository {
    fun getCategories(): LiveData<List<Category>>
}