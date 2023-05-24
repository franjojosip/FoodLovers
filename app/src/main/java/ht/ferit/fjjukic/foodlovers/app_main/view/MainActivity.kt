package ht.ferit.fjjukic.foodlovers.app_main.view

import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseActivity
import ht.ferit.fjjukic.foodlovers.app_main.view_model.MainActivityViewModel
import ht.ferit.fjjukic.foodlovers.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {

    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()

    override fun init() {
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_search_recipes, R.id.nav_search_category, R.id.nav_show_recipe -> {
                    binding.clBottomBar.isVisible = false
                }

                else -> {
                    binding.clBottomBar.isVisible = true
                }

            }
        }
    }

}