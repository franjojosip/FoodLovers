package ht.ferit.fjjukic.foodlovers.app_main.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_main.prelogin.PreloginActivity
import ht.ferit.fjjukic.foodlovers.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MainActivityListener {

    private val destinationsWithoutBottomBar = listOf(
        R.id.nav_filter_recipes,
        R.id.nav_search_recipes,
        R.id.nav_search_category,
        R.id.nav_show_recipe,
        R.id.nav_create_recipe,
        R.id.nav_edit_recipe,
        R.id.nav_change_email,
        R.id.nav_change_username,
        R.id.nav_change_location
    )

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment).navController
    }

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            checkUser()
            when {
                destinationsWithoutBottomBar.contains(destination.id) -> {
                    binding.bottomNav.visibility = View.GONE
                }

                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }
        binding.bottomNav.setOnItemReselectedListener { }
    }

    override fun onResume() {
        super.onResume()
        checkUser()
    }

    private fun checkUser() {
        if (!viewModel.isLoggedIn()) {
            navigateToPrelogin()
        }
    }

    override fun navigateToPrelogin() {
        val intent = Intent(this@MainActivity, PreloginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}