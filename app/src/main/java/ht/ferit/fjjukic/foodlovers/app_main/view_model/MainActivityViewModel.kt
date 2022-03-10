package ht.ferit.fjjukic.foodlovers.app_main.view_model

import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class MainActivityViewModel(
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    fun isUserAuthenticated(): Boolean {
        return preferenceManager.user != null
    }
}