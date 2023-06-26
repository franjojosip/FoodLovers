package ht.ferit.fjjukic.foodlovers.app_main.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class NavigationActivityViewModel(
    private val preferenceManager: PreferenceManager,
    private val firebaseSource: FirebaseSource
) : BaseViewModel() {

    private var _user: MutableLiveData<UserModel> = MutableLiveData(preferenceManager.user)
    val user: LiveData<UserModel> = _user

    fun logout() {
        firebaseSource.logout()
        _actionNavigate.postValue(ActionNavigate.Login)
    }

    fun handleNavigate(actionNavigate: ActionNavigate) {
        _actionNavigate.postValue(actionNavigate)
    }

    fun refreshUser() {
        _user.postValue(preferenceManager.user)
    }
}