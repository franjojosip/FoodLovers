package ht.ferit.fjjukic.foodlovers.app_auth.viewmodel

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.view.ResetPasswordFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository

class ResetPasswordViewModel(
    private val userRepository: UserRepository,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

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