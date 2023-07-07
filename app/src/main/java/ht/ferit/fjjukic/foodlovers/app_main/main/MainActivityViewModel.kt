package ht.ferit.fjjukic.foodlovers.app_main.main

import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel

class MainActivityViewModel(
    val preferenceManager: PreferenceManager
) : BaseViewModel() {

    fun isLoggedIn(): Boolean {
        return preferenceManager.user != null
    }
}