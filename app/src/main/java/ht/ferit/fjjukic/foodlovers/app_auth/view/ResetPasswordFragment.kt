package ht.ferit.fjjukic.foodlovers.app_auth.view

import androidx.core.widget.doOnTextChanged
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentResetPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : BaseFragment<AuthViewModel, FragmentResetPasswordBinding>() {

    override val layoutId = R.layout.fragment_reset_password
    override val viewModel: AuthViewModel by viewModel()

    override fun init() {
        setListeners()
    }

    private fun setListeners() {
        binding.tilEmail.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.validateField(FieldValidator::checkEmail)
        }

        binding.btnResetPassword.setOnClickListener {
            binding.tilEmail.validateField(FieldValidator::checkEmail)
            if (!binding.tilEmail.isErrorEnabled) {
                viewModel.resetPassword(binding.tilEmail.getValue())
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            viewModel.handleBackToLogin()
        }
    }
}
