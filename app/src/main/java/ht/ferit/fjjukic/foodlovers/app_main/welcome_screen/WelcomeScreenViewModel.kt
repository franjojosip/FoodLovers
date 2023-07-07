package ht.ferit.fjjukic.foodlovers.app_main.welcome_screen

import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel

class WelcomeScreenViewModel(
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

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