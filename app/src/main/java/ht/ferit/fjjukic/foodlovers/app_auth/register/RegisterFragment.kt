package ht.ferit.fjjukic.foodlovers.app_auth.register

import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.login.LoginFragment
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.*
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding>() {

    override val layoutId: Int = R.layout.fragment_register
    override val viewModel: AuthViewModel by viewModel()

    override fun init() {
        setupClickListener()
        setupInputListener()
        setupObservers()
    }

    private fun setupClickListener() {
        binding.tvNavigateToLogin.setOnClickListener {
            viewModel.handleNavigateAction(ActionNavigate.Login)
        }

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                binding.tilUsername.getValue(),
                binding.tilEmail.getValue(),
                binding.tilPassword.getValue()
            )
        }
    }

    private fun setupInputListener() {

        binding.tilUsername.editText?.subscribeTextChanges {
            binding.tilUsername.validateField(FieldValidator::checkUsername, ::toggleRegisterButton)
        }

        binding.tilEmail.editText?.subscribeTextChanges {
            binding.tilEmail.validateField(FieldValidator::checkEmail, ::toggleRegisterButton)
        }

        binding.tilPassword.editText?.subscribeTextChanges {
            binding.tilUsername.validateField(FieldValidator::checkUsername, ::toggleRegisterButton)
        }

        binding.tilRepeatedPassword.editText?.subscribeTextChanges {
            binding.tilRepeatedPassword.toggleError(
                FieldValidator.checkRepeatedPassword(
                    binding.tilPassword.getValue(),
                    it
                )
            )
            toggleRegisterButton()
        }

        binding.tilRepeatedPassword.editText?.hideKeyboardOnActionDone()
    }

    private fun setupObservers() {
        viewModel.showMessage.observeNotNull(viewLifecycleOwner) {
            showToast(it.message, it.messageId)
        }

        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            if (it is ActionNavigate.Home) {
                //requireContext().startNavigationActivity()
            } else if (it is ActionNavigate.Login) {
                requireActivity().navigateToScreen(LoginFragment())
            }
        }

        viewModel.showLoading.observeNotNull(viewLifecycleOwner) {
            binding.loaderLayout.isVisible = it
        }
    }

    private fun toggleRegisterButton() {
        binding.btnRegister.isEnabled = viewModel.isValidRegistrationCredentials(
            binding.tilUsername.getValue(),
            binding.tilEmail.getValue(),
            binding.tilPassword.getValue(),
            binding.tilRepeatedPassword.getValue(),
        )
    }
}
