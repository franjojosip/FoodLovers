package ht.ferit.fjjukic.foodlovers.app_main.view_model

import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_main.view.WelcomeScreenFragmentDirections

class WelcomeScreenViewModel(
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    val userAuthenticated = preferenceManager.user != null
    val firstTime = preferenceManager.isFirstTime

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