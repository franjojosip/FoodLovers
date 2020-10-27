package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.FragmentResetPasswordBinding
import ht.ferit.fjjukic.foodlovers.ui.base.AuthViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.AuthViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ForgotPasswordFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()

    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var tvLogin: TextView
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding = FragmentResetPasswordBinding.inflate(
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
        tvLogin = view.findViewById(R.id.tvLogin)
        tvLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, LoginFragment()).commit()
        }
    }

    override fun onSuccess() {
        Toast.makeText(context, "Reset password link sent to your email", Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, LoginFragment()).commit()
    }

    override fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
