package ht.ferit.fjjukic.foodlovers.app_account.change_email.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeEmailViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val currentUser: LiveData<UserModel> = _user

    init {
        viewModelScope.launch(Dispatchers.Main) {
            _user.value = preferenceManager.user
        }
    }

    fun updateEmail(email: String, password: String) {
        val user = currentUser.value
        if (user != null) {
//            firebaseSource.reauthenticateUser(firebaseUser, user.email, password)
//                .flatMap {
//                    if (it) {
//                        firebaseSource.updateEmail(firebaseUser, email)
//                    } else {
//                        Observable.just(false)
//                    }
//                }
//                .flatMap {
//                    if (it) {
//                        user.email = email
//                        userRepository.update(user)
//                    } else {
//                        Observable.just(false)
//                    }
//                }
//                .subscribeIO()
//                .observeMain()
//                .subscribeWithResult({
//                    if (it) {
//                        showMessage(messageId = R.string.email_change_success)
//                        firebaseSource.logout()
//                        actionNavigate.postValue(ActionNavigate.Login)
//                    } else {
//                        showMessage(messageId = R.string.email_change_server_error)
//                    }
//                }, ::handleError)
        }
    }

    fun checkEmail(value: String): Int? {
        return FieldValidator.checkEmail(value)
            ?: FieldValidator.checkSameEmail(currentUser.value?.email ?: "", value)
    }

    fun isValidChangeEmailCredentials(email: String, password: String): Boolean {
        return FieldValidator.checkEmail(email) == null
                && FieldValidator.checkSameEmail(currentUser.value?.email ?: "", email) == null
                && FieldValidator.checkPassword(password) == null
    }
}