package ht.ferit.fjjukic.foodlovers.app_account.view

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.ChangeUsernameViewModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.clearFocusAndHideKeyboard
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentChangeUsernameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeUsernameFragment :
    BaseFragment<ChangeUsernameViewModel, FragmentChangeUsernameBinding>() {
    override val layoutId: Int = R.layout.fragment_change_username
    override val viewModel: ChangeUsernameViewModel by viewModel()

    override fun init() {
        viewModel.init()
        setScreen()
    }

    private fun setScreen() {
        binding.btnUpdate.setOnClickListener {
            binding.tilUsername.validateField(viewModel::checkUsername)

            if (!binding.tilUsername.isErrorEnabled) {
                binding.tilUsername.clearFocusAndHideKeyboard()

                viewModel.updateUsername(binding.tilUsername.getValue())
            }
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.user.observeNotNull(viewLifecycleOwner) {
            binding.tilUsername.editText?.setText(it.name)
        }
    }
}