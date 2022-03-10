package ht.ferit.fjjukic.foodlovers.app_account.change_username.view

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.change_username.view_model.ChangeUsernameViewModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionEvent
import ht.ferit.fjjukic.foodlovers.app_common.utils.*
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
        setUpClickListeners()
        setUpInputListeners()
        setUpObservers()
    }

    private fun setUpClickListeners() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateUsername(binding.tilUsername.getValue())
        }
    }

    private fun setUpInputListeners() {
        binding.tilUsername.editText?.subscribeTextChanges {
            binding.tilUsername.validateField(
                viewModel::checkUsername,
                ::toggleChangeUsernameButton
            )
        }
        binding.tilUsername.editText?.hideKeyboardOnActionDone()
    }

    private fun setUpObservers() {
        viewModel.currentUser.observeNotNull(viewLifecycleOwner) {
            binding.tilUsername.editText?.setText(it.name)
        }

        viewModel.showMessage.observeNotNull(viewLifecycleOwner) {
            showToast(it.message, it.messageId)
        }

        viewModel.showLoading.observeNotNull(viewLifecycleOwner) {
            binding.loaderLayout.isVisible = it
        }

        viewModel.refreshUser.observeNotNull(viewLifecycleOwner) {
            EventBus.getDefault().post(ActionEvent.UserChange)
            //view?.clearFocusAndHideKeyboard()
            findNavController().navigateUp()
        }
    }

    private fun toggleChangeUsernameButton() {
        binding.btnUpdate.isEnabled = viewModel.checkUsername(binding.tilUsername.getValue()) == null
    }
}