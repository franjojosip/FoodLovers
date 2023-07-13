package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.app_auth.view.LoginFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository

class LoginViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {
    fun login(email: String, password: String) {
        handleResult({
            userRepository.login(email, password)
        }, {
            actionNavigate.postValue(ActionNavigate.MainScreen)
        }, {
            showSnackbar(it?.message)
        })
    }

    fun handleForgotPasswordClick() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                LoginFragmentDirections.actionNavLoginToNavResetPassword()
            )
        )
    }

    fun handleRegistrationClick() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                LoginFragmentDirections.actionNavLoginToNavRegister()
            )
        )
    }
}