package ht.ferit.fjjukic.foodlovers.app_main.viewmodel

import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_main.view.WelcomeScreenFragmentDirections

class WelcomeScreenViewModel(
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

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