package ht.ferit.fjjukic.foodlovers.app_common.shared_preferences

import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel

interface PreferenceManager {
    var user: UserModel?
    var userId: String?
    var isFirstTime: Boolean
    var lastUpdatedRecipes: Long
    var lastUpdatedCategories: Long
    var lastUpdatedDifficulties: Long
}