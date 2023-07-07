package ht.ferit.fjjukic.foodlovers.app_main.prelogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_main.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreloginActivity : AppCompatActivity(R.layout.activity_prelogin), PreloginListener {
    private val viewModel: PreloginViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        if (viewModel.isLoggedIn()) {
            navigateToMainActivity()
        }
    }

    override fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}