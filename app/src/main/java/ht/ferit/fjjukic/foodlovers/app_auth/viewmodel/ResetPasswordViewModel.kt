package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.view.ResetPasswordFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel

class ResetPasswordViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

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

    fun handleBackToLogin() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                ResetPasswordFragmentDirections.actionNavResetPasswordToNavLogin()
            )
        )
    }
}