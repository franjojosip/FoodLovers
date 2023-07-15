package ht.ferit.fjjukic.foodlovers.app_main.main

import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val difficultyRepository: DifficultyRepository,
    private val categoryRepository: CategoryRepository,
    private val preferenceManager: PreferenceManager,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            difficultyRepository.getDifficulties()
            categoryRepository.getCategories()
        }
    }

    fun isLogged(): Boolean {
        return preferenceManager.user != null
    }
}