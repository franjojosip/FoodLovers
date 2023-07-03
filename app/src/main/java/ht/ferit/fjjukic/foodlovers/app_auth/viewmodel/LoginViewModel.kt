package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.app_auth.view.LoginFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class LoginViewModel(
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
}