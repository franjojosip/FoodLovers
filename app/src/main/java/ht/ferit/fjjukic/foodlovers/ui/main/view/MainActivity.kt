package ht.ferit.fjjukic.foodlovers.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.ui.main.view.ui.auth.LoginFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        /* FirebaseDB().getUser(viewModel.firebaseUser!!.uid, object : FirebaseDatabaseCallback {
            override fun <T : Any> onCallback(value: T) {
                val model = value as UserModel
                Log.d("DISU", model.userId)
                Log.d("DISU", model.name)
                Log.d("DISU", model.email)
                Log.d("DISU", model.latitude)
                Log.d("DISU", model.longitude)
            }
        })
        FirebaseDB().postFoodType("Sweet")
        FirebaseDB().postFoodType("Salty")
         */
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, LoginFragment()).commit()
    }
}