package ht.ferit.fjjukic.foodlovers.ui.main.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.LoginActivityBinding
import ht.ferit.fjjukic.foodlovers.ui.base.AuthViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.utils.startMenuActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LoginActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var ivShowPassword: ImageView
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.hide()

        val binding: LoginActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.login_activity)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
        init()
    }

    private fun init() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        ivShowPassword = findViewById(R.id.iv_show_password)

        ivShowPassword.setOnClickListener {
            if (etPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivShowPassword.setImageResource(R.drawable.ic_show_password)
            } else {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivShowPassword.setImageResource(R.drawable.ic_hide_password)
            }
        }
    }

    override fun onSuccess() {
        Toast.makeText(this, "User successfully logged in", Toast.LENGTH_SHORT).show()
        startMenuActivity()
    }

    override fun onFailure(message: String) {
        etEmail.setBackgroundResource(R.drawable.error_background)
        etPassword.setBackgroundResource(R.drawable.error_background)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.firebaseUser?.let {
            startMenuActivity()
        }
    }
}