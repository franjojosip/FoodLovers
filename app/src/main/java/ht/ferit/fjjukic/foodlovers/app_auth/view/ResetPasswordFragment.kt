package ht.ferit.fjjukic.foodlovers.app_auth.view

import androidx.core.widget.doOnTextChanged
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.ResetPasswordViewModel
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.databinding.FragmentResetPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : BaseFragment<ResetPasswordViewModel, FragmentResetPasswordBinding>() {

    override val layoutId = R.layout.fragment_reset_password
    override val viewModel: ResetPasswordViewModel by viewModel()

    override fun init() {
        loader = binding.loaderLayout
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
