package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.view.LoginFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_auth.view.RegisterFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_auth.view.ResetPasswordFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class AuthViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {
    fun login(email: String, password: String) {
        handleResult({
            userRepository.login(email, password)
        }, {
            _actionNavigate.postValue(
                ActionNavigate.NavigationWithDirections(
                    LoginFragmentDirections.actionNavLoginToNavGraphHome()
                )
            )
        }, {
            showSnackbar(it?.message)
        })
    }

    fun register(username: String, email: String, password: String) {
        handleResult({
            userRepository.register(email, username, password)
        }, {
            _actionNavigate.postValue(
                ActionNavigate.NavigationWithDirections(
                    RegisterFragmentDirections.actionNavRegisterToNavGraphHome()
                )
            )
        }, {
            showSnackbar(it?.message)
        })
    }

    fun resetPassword(email: String) {
        handleResult({
            userRepository.resetPassword(email)
        }, {
            showMessage(messageId = R.string.password_reset_success)
            handleBackToLogin()
        }, {
            showSnackbar(it?.message)
        })
    }

    fun handleForgotPasswordClick() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                LoginFragmentDirections.actionNavLoginToNavResetPassword()
            )
        )
    }

    fun handleRegistrationClick() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                LoginFragmentDirections.actionNavLoginToNavRegister()
            )
        )
    }

    fun onNavigateToLoginClick() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                RegisterFragmentDirections.actionNavRegisterToNavLogin()
            )
        )
    }

    fun handleBackToLogin() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                ResetPasswordFragmentDirections.actionNavResetPasswordToNavLogin()
            )
        )
    }
}