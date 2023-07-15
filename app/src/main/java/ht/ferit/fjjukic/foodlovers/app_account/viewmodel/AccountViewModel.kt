package ht.ferit.fjjukic.foodlovers.app_account.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val user: LiveData<UserModel> = _user

    fun init() {
        _user.value = preferenceManager.user
    }

    fun handleImagePathChange(value: Uri) {
        handleResult({
            userRepository.updateUserImage(value)
        }, {
            when (it) {
                true -> {
                    _user.postValue(preferenceManager.user)
                    showMessage(messageId = R.string.image_change_success)
                }

                else -> {
                    showMessage(messageId = R.string.image_change_error)
                }
            }
        }, {
            showMessage(messageId = R.string.image_change_error)
        })
    }

    fun handleNavigation(action: ActionNavigate) {
        actionNavigate.postValue(action)
    }

    fun onLogoutClick() {
        viewModelScope.launch(Dispatchers.IO) {
            screenEvent.postValue(LoadingBar(true))

            userRepository.logout()
            actionNavigate.postValue(ActionNavigate.Prelogin)

            screenEvent.postValue(LoadingBar(false))
        }
    }
}
