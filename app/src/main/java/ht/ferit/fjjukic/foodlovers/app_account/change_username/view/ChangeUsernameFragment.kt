package ht.ferit.fjjukic.foodlovers.app_account.change_username.view

import androidx.navigation.fragment.findNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.change_username.view_model.ChangeUsernameViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionEvent
import ht.ferit.fjjukic.foodlovers.app_common.utils.clearFocusAndHideKeyboard
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.hideKeyboardOnActionDone
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentChangeUsernameBinding
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeUsernameFragment :
    BaseFragment<ChangeUsernameViewModel, FragmentChangeUsernameBinding>() {
    override val layoutId: Int = R.layout.fragment_change_username
    override val viewModel: ChangeUsernameViewModel by viewModel()

    override fun init() {
        viewModel.init()
        setView()
    }

    private fun setView() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateUsername(binding.tilUsername.getValue())

            binding.tilUsername.editText?.subscribeTextChanges {
                binding.tilUsername.validateField(
                    viewModel::checkUsername
                )
            }
            binding.tilUsername.editText?.hideKeyboardOnActionDone()
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.currentUser.observeNotNull(viewLifecycleOwner) {
            binding.tilUsername.editText?.setText(it.name)
        }

        viewModel.showMessage.observeNotNull(viewLifecycleOwner) {
            showToast(it.message, it.messageId)
        }

        viewModel.refreshUser.observeNotNull(viewLifecycleOwner) {
            EventBus.getDefault().post(ActionEvent.UserChange)
            binding.root.clearFocusAndHideKeyboard()
            findNavController().navigateUp()
        }
    }

    private fun toggleChangeUsernameButton() {
        binding.btnUpdate.isEnabled =
            viewModel.checkUsername(binding.tilUsername.getValue()) == null
    }
}