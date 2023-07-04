package ht.ferit.fjjukic.foodlovers.app_main.viewmodel

import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_main.view.WelcomeScreenFragmentDirections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WelcomeScreenViewModel(
    private val preferenceManager: PreferenceManager,
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository
) : BaseViewModel() {

    init {
        if (preferenceManager.lastUpdatedCategories == 0L) {
            GlobalScope.launch {
                categoryRepository.getCategories()
            }
        }
        if (preferenceManager.lastUpdatedDifficulties == 0L) {
            GlobalScope.launch {
                difficultyRepository.getDifficulties()
            }
        }
    }

    fun init() {
        if (!preferenceManager.isFirstTime) {
            handleWelcomeScreenNavigation()
        }
    }

    fun onStartClick() {
        preferenceManager.isFirstTime = false
        handleWelcomeScreenNavigation()
    }

    private fun handleWelcomeScreenNavigation() {
        when {
            preferenceManager.user != null -> {
                actionNavigate.postValue(
                    ActionNavigate.NavigationWithDirections(
                        WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphBottom()
                    )
                )
            }

            else -> {
                actionNavigate.postValue(
                    ActionNavigate.NavigationWithDirections(
                        WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphAuth()
                    )
                )
            }
        }
    }
}