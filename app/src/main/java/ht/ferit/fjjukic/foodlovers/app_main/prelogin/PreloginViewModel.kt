package ht.ferit.fjjukic.foodlovers.app_main.prelogin

import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager

class PreloginViewModel(
    val preferenceManager: PreferenceManager,
    difficultyRepository: DifficultyRepository,
    categoryRepository: CategoryRepository,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {
    init {
        difficultyRepository.init()
        categoryRepository.init()
    }

    fun isLoggedIn(): Boolean {
        return preferenceManager.user != null
    }
}