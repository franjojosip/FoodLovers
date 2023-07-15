package ht.ferit.fjjukic.foodlovers.app_account.view

import androidx.core.widget.doOnTextChanged
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.ChangeEmailViewModel
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.app_common.utils.getValue
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.validateField
import ht.ferit.fjjukic.foodlovers.databinding.FragmentChangeEmailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeEmailFragment : BaseFragment<ChangeEmailViewModel, FragmentChangeEmailBinding>() {
    override val layoutId: Int = R.layout.fragment_change_email
    override val viewModel: ChangeEmailViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        viewModel.logScreenEvent(FirebaseAnalyticsConstants.Event.Screen.CHANGE_EMAIL)
    }

    override fun init() {
        loader = binding.loaderLayout
        setScreen()
    }

    private fun setScreen() {
        binding.btnUpdate.setOnClickListener {
            binding.tilNewEmail.validateField(viewModel::checkEmail)

            if (!binding.tilNewEmail.isErrorEnabled) {
                viewModel.updateEmail(binding.tilNewEmail.getValue())
            }
        }
        binding.tilNewEmail.editText?.doOnTextChanged { _, _, _, _ ->
            binding.tilNewEmail.validateField(viewModel::checkEmail)
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.user.observeNotNull(viewLifecycleOwner) {
            binding.tilOldEmail.editText?.setText(it.email)
        }
    }
}