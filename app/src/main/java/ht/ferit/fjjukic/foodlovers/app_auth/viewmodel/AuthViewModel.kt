package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class AuthViewModel(
    private val firebaseSource: FirebaseSource,
    private val preferenceManager: PreferenceManager,
    private val repository: UserRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val actionNavigate: LiveData<ActionNavigate> = _actionNavigate
    val showMessage: LiveData<MessageModel> = _showMessage
    val showLoading: LiveData<Boolean> = _showLoading

    fun init() {
        preferenceManager.user?.let {
            _actionNavigate.postValue(ActionNavigate.Home)
        }
    }

    fun login(email: String, password: String) {
        firebaseSource.login(email, password)
            .flatMap { userId ->
                userRepository.get(userId)
            }
            .subscribeIO()
            .observeMain()
            .subscribeWithResult({
                _actionNavigate.postValue(ActionNavigate.Home)
            }, ::handleError)
    }

    fun register(username: String, email: String, password: String) {
        firebaseSource.register(email, username, password)
            .flatMap {
                repository.insert(it)
            }
            .subscribeIO()
            .observeMain()
            .subscribeWithResult({
                showMessage(messageId = R.string.register_success)
                _actionNavigate.postValue(ActionNavigate.Login)
            }, ::handleError)
    }

    fun resetPassword(email: String) {
        firebaseSource.resetPassword(email)
            .subscribeIO()
            .observeMain()
            .subscribeWithResult({
                showMessage(messageId = R.string.password_reset_success)
                _actionNavigate.postValue(ActionNavigate.Login)
            }, ::handleError)
    }

    fun isValidLoginCredentials(email: String, password: String): Boolean {
        return FieldValidator.checkEmail(email) == null && FieldValidator.checkPassword(password) == null
    }

    fun isValidRegistrationCredentials(
        username: String,
        email: String,
        password: String,
        repeatedPassword: String
    ): Boolean {
        return FieldValidator.checkUsername(username) == null && FieldValidator.checkEmail(email) == null
                && FieldValidator.checkPassword(password) == null
                && FieldValidator.checkRepeatedPassword(password, repeatedPassword) == null
    }

    fun isValidForgotPasswordCredentials(email: String): Boolean {
        return FieldValidator.checkEmail(email) == null
    }

    fun handleNavigateAction(action: ActionNavigate) {
        _actionNavigate.postValue(action)
    }
}