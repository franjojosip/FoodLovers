package ht.ferit.fjjukic.foodlovers.app_auth.forgotpassword

import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.login.LoginFragment
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.*
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentResetPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : BaseFragment<AuthViewModel, FragmentResetPasswordBinding>() {

    override val layoutId = R.layout.fragment_reset_password
    override val viewModel: AuthViewModel by viewModel()

    override fun init() {
        setUpClickListeners()
        setUpInputListeners()
        setUpObservers()
    }

    private fun setUpClickListeners() {
        binding.btnResetPassword.setOnClickListener {
            viewModel.resetPassword(binding.tilEmail.getValue())
        }

        binding.tvBackToLogin.setOnClickListener {
            viewModel.handleNavigateAction(ActionNavigate.Login)
        }
    }

    private fun setUpInputListeners() {
        binding.tilEmail.editText?.subscribeTextChanges {
            binding.tilEmail.validateField(FieldValidator::checkEmail, ::toggleResetPasswordButton)
        }

        binding.tilEmail.editText?.hideKeyboardOnActionDone()
    }

    private fun toggleResetPasswordButton() {
        binding.btnResetPassword.isEnabled = viewModel.isValidForgotPasswordCredentials(binding.tilEmail.getValue())
    }

    private fun setUpObservers() {
        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            if (it is ActionNavigate.Login) {
                requireActivity().navigateToScreen(LoginFragment())
            }
        }

        viewModel.showMessage.observeNotNull(viewLifecycleOwner) {
            showToast(it.message, it.messageId)
        }

        viewModel.showLoading.observeNotNull(viewLifecycleOwner) {
            binding.loaderLayout.isVisible = it
        }
    }
}
