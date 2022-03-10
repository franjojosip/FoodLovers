package ht.ferit.fjjukic.foodlovers.app_account.change_username.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class ChangeUsernameViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val currentUser: LiveData<UserModel> = _user


    private val _refreshUser: SingleLiveData<Boolean> = SingleLiveData()
    val refreshUser: LiveData<Boolean> = _refreshUser

    val showMessage: LiveData<MessageModel> = _showMessage
    val showLoading: LiveData<Boolean> = _showLoading

    fun init() {
        _user.postValue(preferenceManager.user)
    }

    fun updateUsername(value: String) {
        preferenceManager.user?.let {
            it.name = value
            userRepository.update(it)
                .subscribeIO()
                .observeMain()
                .subscribeWithResult({ isSuccess ->
                    if (isSuccess) {
                        showMessage(messageId = R.string.username_change_success)
                        _refreshUser.postValue(true)
                    } else {
                        showMessage(messageId = R.string.username_change_error)
                    }
                }, ::handleError)
        }
    }

    fun checkUsername(value: String): Int? {
        return FieldValidator.checkUsername(value) ?: FieldValidator.checkSameUsername(currentUser.value?.name ?: "", value)
    }
}