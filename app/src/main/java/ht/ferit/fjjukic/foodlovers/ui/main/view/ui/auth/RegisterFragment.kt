package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRegisterBinding
import ht.ferit.fjjukic.foodlovers.ui.base.AuthViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.AuthViewModel
import ht.ferit.fjjukic.foodlovers.utils.startNavigationActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RegisterFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var ivShowPassword: ImageView
    private lateinit var tvLogin: TextView
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding = FragmentRegisterBinding.inflate(
            inflater,
            container,
            false
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_password)
        ivShowPassword = view.findViewById(R.id.iv_show_password)
        tvLogin = view.findViewById(R.id.tvLogin)

        ivShowPassword.setOnClickListener {
            if (etPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivShowPassword.setImageResource(R.drawable.ic_show_password)
            } else {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivShowPassword.setImageResource(R.drawable.ic_hide_password)
            }
        }
        tvLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, LoginFragment()).commit()
        }

    }

    override fun onSuccess() {
        Toast.makeText(context, "User has been successfully registered!", Toast.LENGTH_SHORT).show()
        requireContext().startNavigationActivity()
    }

    override fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
