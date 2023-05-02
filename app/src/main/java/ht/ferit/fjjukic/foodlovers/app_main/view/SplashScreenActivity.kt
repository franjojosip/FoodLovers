package ht.ferit.fjjukic.foodlovers.app_main.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.startMainActivity
import ht.ferit.fjjukic.foodlovers.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {

    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartCooking.setOnClickListener {
            startMainActivity()
        }
    }
}