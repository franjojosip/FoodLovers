package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.app_auth.view.RegisterFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class RegisterViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

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

    fun onNavigateToLoginClick() {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                RegisterFragmentDirections.actionNavRegisterToNavLogin()
            )
        )
    }
}