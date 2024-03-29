package ht.ferit.fjjukic.foodlovers.app_common.shared_preferences

import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel

interface PreferenceManager {
    var user: UserModel?
    var isFirstTime: Boolean
    var lastUpdatedRecipes: Long
    var lastUpdatedCategories: Long
    var lastUpdatedDifficulties: Long
    var favoriteRecipeIds: List<String>
}