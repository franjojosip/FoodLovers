package ht.ferit.fjjukic.foodlovers.app_auth.view

import androidx.core.widget.doOnTextChanged
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.toggleError
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding>() {

    override val layoutId: Int = R.layout.fragment_register
    override val viewModel: AuthViewModel by viewModel()

    override fun init() {
        setListeners()
    }

    private fun setListeners() {
        binding.tvNavigateToLogin.setOnClickListener {
            viewModel.onNavigateToLoginClick()
        }

        binding.btnRegister.setOnClickListener {
            binding.tilUsername.validateField(FieldValidator::checkUsername)
            binding.tilEmail.validateField(FieldValidator::checkEmail)
            binding.tilPassword.validateField(FieldValidator::checkPassword)
            binding.tilRepeatedPassword.toggleError(
                FieldValidator.checkRepeatedPassword(
                    binding.tilPassword.getValue(),
                    binding.tilRepeatedPassword.getValue()
                )
            )

            if (!binding.tilUsername.isErrorEnabled && !binding.tilEmail.isErrorEnabled &&
                !binding.tilPassword.isErrorEnabled && !binding.tilRepeatedPassword.isErrorEnabled
            ) {
                viewModel.register(
                    binding.tilUsername.getValue(),
                    binding.tilEmail.getValue(),
                    binding.tilPassword.getValue()
                )
            }
        }

        binding.tilUsername.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilUsername.validateField(FieldValidator::checkUsername)
        }

        binding.tilEmail.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.validateField(FieldValidator::checkEmail)
        }

        binding.tilPassword.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilUsername.validateField(FieldValidator::checkPassword)
        }

        binding.tilRepeatedPassword.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilRepeatedPassword.toggleError(
                FieldValidator.checkRepeatedPassword(
                    binding.tilPassword.getValue(),
                    binding.tilRepeatedPassword.getValue()
                )
            )
        }
    }
}
