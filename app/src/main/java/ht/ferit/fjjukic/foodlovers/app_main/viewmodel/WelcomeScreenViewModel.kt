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

    val userAuthenticated = preferenceManager.user != null
    val firstTime = preferenceManager.isFirstTime

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

    fun onStartClick() {
        preferenceManager.isFirstTime = false

        when {
            userAuthenticated -> {
                _actionNavigate.postValue(
                    ActionNavigate.NavigationWithDirections(
                        WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphHome()
                    )
                )
            }

            else -> {
                _actionNavigate.postValue(
                    ActionNavigate.NavigationWithDirections(
                        WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphAuth()
                    )
                )
            }
        }
    }
}