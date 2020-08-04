package ht.ferit.fjjukic.foodlovers.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.MenuActivityBinding
import ht.ferit.fjjukic.foodlovers.ui.base.MenuViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.MenuViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MenuActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<MenuViewModelFactory>()
    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)
        supportActionBar?.hide()

        val binding: MenuActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.menu_activity)
        viewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)
        binding.viewmodel = viewModel
        init()
    }

    private fun init() {
        viewModel.getFoodTypes().observe(this, Observer {
            viewModel.foodTypes.value = it
        })
    }

}