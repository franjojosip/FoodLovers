package ht.ferit.fjjukic.foodlovers.app_account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator

class ChangeEmailViewModel(
    private val userRepository: UserRepository,
    preferenceManager: PreferenceManager,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val user: LiveData<UserModel> = _user

    init {
        _user.value = preferenceManager.user
    }

    fun updateEmail(email: String) {
        handleResult({
            userRepository.updateEmail(email)
        }, {
            when (it) {
                true -> {
                    showSnackbar(messageId = R.string.email_change_success)
                    actionNavigate.postValue(ActionNavigate.Back)
                }

                else -> {
                    showSnackbar(messageId = R.string.email_change_field_error)
                }
            }
        }, {
            showSnackbar(messageId = R.string.email_change_server_error)
        })
    }

    fun checkEmail(value: String): Int? {
        return FieldValidator.checkEmail(value)
            ?: FieldValidator.checkSameEmail(user.value?.email ?: "", value)
    }
}