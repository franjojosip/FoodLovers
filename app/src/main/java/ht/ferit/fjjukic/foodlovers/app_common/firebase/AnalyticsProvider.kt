package ht.ferit.fjjukic.foodlovers.app_common.firebase

interface AnalyticsProvider {
    fun logScreenEvent(screenName: String)
    fun logShowRecipeScreenEvent(recipeId: String)
    fun logCategoryRecipesScreenEvent(category: String)
}