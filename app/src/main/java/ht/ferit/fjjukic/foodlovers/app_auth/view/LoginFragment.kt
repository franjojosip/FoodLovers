package ht.ferit.fjjukic.foodlovers.app_auth.view

import androidx.core.widget.doOnTextChanged
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.LoginViewModel
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.utils.clearFocusAndHideKeyboard
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val layoutId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModel()

    override fun init() {
        loader = binding.loaderLayout
        setListeners()
    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            binding.tilEmail.validateField(FieldValidator::checkEmail)
            binding.tilPassword.validateField(FieldValidator::checkPassword)

            binding.root.clearFocusAndHideKeyboard()

            if (!binding.tilEmail.isErrorEnabled && !binding.tilPassword.isErrorEnabled) {
                viewModel.login(binding.tilEmail.getValue(), binding.tilPassword.getValue())
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            viewModel.handleForgotPasswordClick()
        }

        binding.tvSignUp.setOnClickListener {
            viewModel.handleRegistrationClick()
        }
        binding.tilEmail.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.validateField(FieldValidator::checkEmail)
        }

        binding.tilPassword.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilPassword.validateField(FieldValidator::checkPassword)
        }
    }
}
