package ht.ferit.fjjukic.foodlovers.app_main.view

import androidx.navigation.fragment.findNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_main.viewmodel.WelcomeScreenViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentWelcomeScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeScreenFragment : BaseFragment<WelcomeScreenViewModel, FragmentWelcomeScreenBinding>() {
    override val layoutId: Int = R.layout.fragment_welcome_screen
    override val viewModel: WelcomeScreenViewModel by viewModel()

    override fun init() {
        if (!viewModel.firstTime) {
            val navDirections =
                when {
                    viewModel.userAuthenticated -> WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphHome()
                    else -> WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphAuth()
                }
            findNavController().navigate(navDirections)
        }

        binding.btnStart.setOnClickListener {
            viewModel.onStartClick()
        }
    }
}