package ht.ferit.fjjukic.foodlovers.app_account.change_email.view

import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.change_email.view_model.ChangeEmailViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.clearFocusAndHideKeyboard
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.hideKeyboardOnActionDone
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.validators.FieldValidator
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentChangeEmailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeEmailFragment :
    BaseFragment<ChangeEmailViewModel, FragmentChangeEmailBinding>() {
    override val layoutId: Int = R.layout.fragment_change_email
    override val viewModel: ChangeEmailViewModel by viewModel()

    override fun init() {
        setView()
    }

    private fun setView() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateEmail(binding.tilNewEmail.getValue(), binding.tilPassword.getValue())
        }
        binding.tilNewEmail.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilNewEmail.validateField(viewModel::checkEmail)
        }

        binding.tilPassword.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilPassword.validateField(FieldValidator::checkPassword)
        }

        binding.tilPassword.editText?.hideKeyboardOnActionDone()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.currentUser.observeNotNull(viewLifecycleOwner) {
            binding.tilOldEmail.editText?.setText(it.email)
        }

        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            binding.root.clearFocusAndHideKeyboard()
            if (it is ActionNavigate.Login) {
                findNavController().navigate(
                    ChangeEmailFragmentDirections.actionNavChangeEmailToNavGraphHome()
                )
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