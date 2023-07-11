package ht.ferit.fjjukic.foodlovers.app_main.prelogin

import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager

class PreloginViewModel(
    val preferenceManager: PreferenceManager
) : BaseViewModel() {

    fun isLoggedIn(): Boolean {
        return preferenceManager.user != null
    }
}