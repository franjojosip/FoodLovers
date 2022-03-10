package ht.ferit.fjjukic.foodlovers.app_main.view

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_auth.login.LoginFragment
import ht.ferit.fjjukic.foodlovers.app_common.utils.navigateToScreen
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseActivity
import ht.ferit.fjjukic.foodlovers.app_main.view_model.MainActivityViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeFragment
import ht.ferit.fjjukic.foodlovers.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()

    override fun init() {
        when {
            true -> {//viewModel.isUserAuthenticated() -> {
                //navigateToScreen(HomeFragment())
                navigateToScreen(CreateRecipeFragment())
            }
            else -> navigateToScreen(LoginFragment(), LoginFragment.TAG)
        }
    }
}