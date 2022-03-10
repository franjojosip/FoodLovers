package ht.ferit.fjjukic.foodlovers.app_account.change_email.view

import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.change_email.view_model.ChangeEmailViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.*
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentChangeEmailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeEmailFragment :
    BaseFragment<ChangeEmailViewModel, FragmentChangeEmailBinding>() {
    override val layoutId: Int = R.layout.fragment_change_email
    override val viewModel: ChangeEmailViewModel by viewModel()

    override fun init() {
        viewModel.init()
        setUpClickListeners()
        setUpInputListeners()
        setUpObservers()
    }

    private fun setUpClickListeners() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateEmail(binding.tilNewEmail.getValue(), binding.tilPassword.getValue())
        }
    }

    private fun setUpInputListeners() {
        binding.tilNewEmail.editText?.subscribeTextChanges {
            binding.tilNewEmail.validateField(viewModel::checkEmail, ::toggleBtnUpdate)
        }

        binding.tilPassword.editText?.subscribeTextChanges {
            binding.tilPassword.validateField(FieldValidator::checkPassword, ::toggleBtnUpdate)
        }

        binding.tilPassword.editText?.hideKeyboardOnActionDone()
    }

    private fun setUpObservers() {
        viewModel.currentUser.observeNotNull(viewLifecycleOwner) {
            binding.tilOldEmail.editText?.setText(it.email)
        }

        viewModel.showMessage.observeNotNull(viewLifecycleOwner) {
            showToast(it.message, it.messageId)
        }

        viewModel.showLoading.observeNotNull(viewLifecycleOwner) {
            binding.loaderLayout.isVisible = it
        }

        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            //clearFocusAndHideKeyboard()
            if (it is ActionNavigate.Login) {
                requireContext().startMainActivity()
                requireActivity().finish()
            }
        }
    }

    private fun toggleBtnUpdate() {
        binding.btnUpdate.isEnabled = viewModel.isValidChangeEmailCredentials(
            binding.tilNewEmail.getValue(),
            binding.tilPassword.getValue()
        )
    }
}