package ht.ferit.fjjukic.foodlovers.app_main.view

import android.os.Bundle
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_main.viewmodel.WelcomeScreenViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentWelcomeScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeScreenFragment : BaseFragment<WelcomeScreenViewModel, FragmentWelcomeScreenBinding>() {
    override val layoutId: Int = R.layout.fragment_welcome_screen
    override val viewModel: WelcomeScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()
    }

    override fun init() {
        binding.btnStart.setOnClickListener {
            viewModel.onStartClick()
        }
    }
}