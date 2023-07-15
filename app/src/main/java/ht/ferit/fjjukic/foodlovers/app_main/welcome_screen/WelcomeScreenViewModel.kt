package ht.ferit.fjjukic.foodlovers.app_main.welcome_screen

import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager

class WelcomeScreenViewModel(
    private val preferenceManager: PreferenceManager,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    val isFirstTime = preferenceManager.isFirstTime
    val user = preferenceManager.user

    fun navigateToAuth() {
        preferenceManager.isFirstTime = false

        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphAuth()
            )
        )
    }
}