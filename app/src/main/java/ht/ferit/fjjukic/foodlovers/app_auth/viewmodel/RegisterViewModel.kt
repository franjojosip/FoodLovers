package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.app_auth.view.RegisterFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository

class RegisterViewModel(
    private val userRepository: UserRepository,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    fun register(username: String, email: String, password: String) {
        handleResult({
            userRepository.register(email, username, password)
        }, {
            actionNavigate.postValue(ActionNavigate.MainScreen)
        }, {
            showSnackbar(it?.message)
        })
    }

    fun onNavigateToLoginClick() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                RegisterFragmentDirections.actionNavRegisterToNavLogin()
            )
        )
    }
}