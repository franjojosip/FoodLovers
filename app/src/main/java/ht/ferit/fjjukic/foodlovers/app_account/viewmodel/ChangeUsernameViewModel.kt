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

class ChangeUsernameViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val user: LiveData<UserModel> = _user

    init {
        _user.value = preferenceManager.user
    }

    fun updateUsername(value: String) {
        val user = preferenceManager.user
        user?.name = value

        user?.let {
            handleResult({
                userRepository.updateUser(user)
            }, {
                showSnackbar(messageId = R.string.username_change_success)
                actionNavigate.postValue(ActionNavigate.Back)
            }, {
                showSnackbar(messageId = R.string.username_change_error)
            })
        } ?: run {
            showSnackbar(messageId = R.string.username_change_error)
        }
    }

    fun checkUsername(value: String): Int? {
        return FieldValidator.checkUsername(value)
            ?: FieldValidator.checkSameUsername(user.value?.name ?: "", value)
    }
}