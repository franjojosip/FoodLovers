package ht.ferit.fjjukic.foodlovers.app_main.welcome_screen

import androidx.navigation.fragment.findNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.databinding.FragmentWelcomeScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeScreenFragment : BaseFragment<WelcomeScreenViewModel, FragmentWelcomeScreenBinding>() {

    override val screenConstant: String = FirebaseAnalyticsConstants.Event.Screen.WELCOME

    override val layoutId: Int = R.layout.fragment_welcome_screen
    override val viewModel: WelcomeScreenViewModel by viewModel()

    override fun init() {
        if (!viewModel.isFirstTime) {
            findNavController().navigate(
                WelcomeScreenFragmentDirections.actionNavWelcomeToNavGraphAuth()
            )
        }

        binding.btnStartCooking.setOnClickListener {
            viewModel.navigateToAuth()
        }
    }
}