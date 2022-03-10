package ht.ferit.fjjukic.foodlovers.app_main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.startMainActivity
import ht.ferit.fjjukic.foodlovers.databinding.FragmentSplashScreenBinding

class SplashScreenActivity: AppCompatActivity() {

    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_splash_screen)
        setContentView(binding.root)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnStartCooking.setOnClickListener {
            startMainActivity()
        }
    }
}