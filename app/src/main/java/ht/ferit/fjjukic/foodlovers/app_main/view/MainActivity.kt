package ht.ferit.fjjukic.foodlovers.app_main.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val destinationsWithoutBottomBar = listOf(
        R.id.nav_welcome,
        R.id.nav_filter_recipes,
        R.id.nav_search_recipes,
        R.id.nav_search_category,
        R.id.nav_show_recipe,
        R.id.nav_create_recipe,
        R.id.nav_profile,
        R.id.nav_change_email,
        R.id.nav_change_username,
        R.id.nav_login,
        R.id.nav_register,
        R.id.nav_reset_password
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when {
                destinationsWithoutBottomBar.contains(destination.id) -> {
                    binding.navigationView.visibility = View.GONE
                }

                else -> {
                    binding.navigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}