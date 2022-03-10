package ht.ferit.fjjukic.foodlovers.app_main.view

import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.listener.LocationHandler
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionEvent
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.startMainActivity
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseActivity
import ht.ferit.fjjukic.foodlovers.app_main.view_model.NavigationActivityViewModel
import ht.ferit.fjjukic.foodlovers.databinding.ActivityNavigationBinding
import ht.ferit.fjjukic.foodlovers.databinding.NavHeaderNavigationBinding
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationActivity : BaseActivity<NavigationActivityViewModel, ActivityNavigationBinding>(),
    LocationHandler {

    override val layoutId: Int = R.layout.activity_navigation
    override val viewModel: NavigationActivityViewModel by viewModel()

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private lateinit var navHeaderBinding: NavHeaderNavigationBinding

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_account, R.id.nav_logout
            ), binding.drawerLayout
        )
    }

    override fun init() {
        if (!::navHeaderBinding.isInitialized) {
            navHeaderBinding = NavHeaderNavigationBinding.bind(binding.navView.getHeaderView(0))
        }
        setUpActionBar()
        setUpProfile()
        setUpObservers()
        setUpListeners()
    }

    @Subscribe
    override fun handleEvent(event: ActionEvent) {
        super.handleEvent(event)
        if (event is ActionEvent.UserChange) {
            viewModel.refreshUser()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navHeaderBinding.unbind()
    }

    private fun setUpActionBar() {
        binding.navView.setupWithNavController(navController)
    }

    private fun setUpProfile() {
        viewModel.user.observeNotNull(this) { user ->
            navHeaderBinding.tvUserName.text = user.name
            navHeaderBinding.tvUserEmail.text = user.email
            navHeaderBinding.tvLocation.text =
                getLocation(baseContext, user.latitude, user.longitude)

            Glide.with(this)
                .load(user.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(navHeaderBinding.ivUserImage)
        }
    }

    private fun setUpObservers() {
        viewModel.actionNavigate.observeNotNull(this) {
            when (it) {
                is ActionNavigate.Login -> {
                    applicationContext.startMainActivity()
                }
                is ActionNavigate.Home -> {
                    navController.navigate(R.id.nav_home)
                }
                is ActionNavigate.Account -> {
                    navController.navigate(R.id.nav_account)
                }
                else -> {}
            }
        }
    }

    private fun setUpListeners() {
        binding.navView.setNavigationItemSelectedListener {
            if (it.itemId != navController.currentDestination?.id) {
                when (it.itemId) {
                    R.id.nav_home -> viewModel.handleNavigate(ActionNavigate.Home)
                    R.id.nav_account -> viewModel.handleNavigate(ActionNavigate.Account)
                    R.id.nav_logout -> viewModel.logout()
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}