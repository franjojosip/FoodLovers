package ht.ferit.fjjukic.foodlovers.app_common.repository.category

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Category

class CategoryRepositoryImpl : CategoryRepository {
    override fun getCategories(): List<Category> {
        return mutableListOf(
            Category("Breakfast", R.drawable.breakfast, hasMarginStart = false),
            Category("Dinner", R.drawable.dinner),
            Category("Lunch", R.drawable.lunch),
            Category("Salad", R.drawable.salad),
            Category("Soup", R.drawable.soup),
            Category("Desert", R.drawable.desert)
        )
    }
}