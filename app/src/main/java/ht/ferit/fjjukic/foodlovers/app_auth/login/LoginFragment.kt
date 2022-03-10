package ht.ferit.fjjukic.foodlovers.app_auth.login

import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.forgotpassword.ForgotPasswordFragment
import ht.ferit.fjjukic.foodlovers.app_auth.register.RegisterFragment
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.*
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_main.view.MainActivity
import ht.ferit.fjjukic.foodlovers.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding>() {

    companion object {
        const val TAG = "LoginFragment"
    }

    override val layoutId: Int = R.layout.fragment_login
    override val viewModel: AuthViewModel by viewModel()

    override fun init() {
        viewModel.init()
        setUpClickListeners()
        setUpInputListeners()
        setUpObservers()
    }

    private fun setUpClickListeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.tilEmail.getValue(), binding.tilPassword.getValue())
        }

        binding.tvForgotPassword.setOnClickListener {
            viewModel.handleNavigateAction(ActionNavigate.ForgotPassword)
        }

        binding.tvSignUp.setOnClickListener {
            viewModel.handleNavigateAction(ActionNavigate.Registration)
        }
    }

    private fun setUpInputListeners() {
        binding.tilEmail.editText?.subscribeTextChanges {
            binding.tilEmail.validateField(FieldValidator::checkEmail, ::toggleLoginButton)
        }

        binding.tilPassword.editText?.subscribeTextChanges {
            binding.tilPassword.validateField(FieldValidator::checkPassword, ::toggleLoginButton)
        }

        binding.tilPassword.editText?.hideKeyboardOnActionDone()
    }

    private fun toggleLoginButton() {
        binding.btnLogin.isEnabled = viewModel.isValidLoginCredentials(
            binding.tilEmail.getValue(),
            binding.tilPassword.getValue()
        )
    }

    private fun setUpObservers() {
        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            when (it) {
                is ActionNavigate.ForgotPassword -> {
                    (activity as MainActivity).navigateToScreen(ForgotPasswordFragment())
                }
                is ActionNavigate.Registration -> {
                    (activity as MainActivity).navigateToScreen(RegisterFragment())
                }
                is ActionNavigate.Home -> {
                    (activity as MainActivity).startNavigationActivity()
                }
                else -> {}
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
