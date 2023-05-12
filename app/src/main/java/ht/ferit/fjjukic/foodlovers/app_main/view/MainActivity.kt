package ht.ferit.fjjukic.foodlovers.app_main.view

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseActivity
import ht.ferit.fjjukic.foodlovers.app_main.view_model.MainActivityViewModel
import ht.ferit.fjjukic.foodlovers.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()


    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            Toast.makeText(this, "trslč", Toast.LENGTH_SHORT).show()

            // if you need to show/hide bottom nav or toolbar based on destination
            // binding.bottomNavigationView.isVisible = destination.id != R.id.itemDetail
        }

    override fun init() {

        binding.fab.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_create_recipe)
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        binding.navigationView.background = null
        binding.navigationView.menu.getItem(2).isEnabled = false

        if (savedInstanceState == null) {
            binding.navigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))
            binding.navigationView.setOnNavigationItemSelectedListener {
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
                true
            }

            binding.bottomAppBar.findNavController()
                .addOnDestinationChangedListener(onDestinationChangedListener)
            binding.bottomAppBar.findNavController()
                .addOnDestinationChangedListener { _, destination, _ ->
                    if (destination.id == R.id.nav_create_recipe) {
                        binding.bottomAppBar.visibility = View.GONE
                    } else {
                        binding.bottomAppBar.visibility = View.VISIBLE
                    }
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}