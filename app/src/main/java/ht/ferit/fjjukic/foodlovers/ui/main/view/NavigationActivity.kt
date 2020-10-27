package ht.ferit.fjjukic.foodlovers.ui.main.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.material.navigation.NavigationView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.NavHeaderNavigationBinding
import ht.ferit.fjjukic.foodlovers.ui.base.UserViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.UserViewModel
import ht.ferit.fjjukic.foodlovers.utils.startMainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class NavigationActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<UserViewModelFactory>()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_account, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setInfo()
    }

    private fun setInfo() {
        val mNavigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = mNavigationView.getHeaderView(0)
        val binding = NavHeaderNavigationBinding.bind(headerView)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        if (viewModel.firebaseUser == null) {
            startMainActivity()
        }
        viewModel.getUser().observe(this, Observer {
            if (it != null) {
                viewModel.setLocation(window.decorView)
                val ivUserImage = window.decorView.findViewById<ImageView>(R.id.iv_user_image)
                Glide.with(this).load(it.imageUrl.toUri())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(ivUserImage)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun goToLogin(item: MenuItem) {
        viewModel.logout(window.decorView)
    }

    fun goToAddRecipe(view: View) {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_recipe, null, null)
    }
}