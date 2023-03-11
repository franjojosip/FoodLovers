package ht.ferit.fjjukic.foodlovers.app_recipe

interface HomeListener {
    fun onCategoryClicked(category: String)
    fun onRecipeClicked(id: String)
}