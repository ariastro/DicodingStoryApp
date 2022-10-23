package io.astronout.dicodingstoryapp.ui.auth.login

import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentLoginBinding
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.showToast
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment(R.layout.fragment_login), LoginContract {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModels()
    private val navController: NavController? by lazy { findNavController() }

    override fun initAction() {
        super.initAction()
        with(binding) {
            tvRegister.setOnClickListener {
                navigateToRegisterScreen()
            }
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                when {
                    !etEmail.isValid -> etEmail.requestFocus()
                    !etPassword.isValid -> etPassword.requestFocus()
                    email.isNotEmpty() && password.isNotEmpty() -> doLogin(email, password)
                }
            }
        }
    }

    override fun doLogin(email: String, password: String) {
        lifecycleScope.launch {
            viewModel.login(email, password).collect {
                when (it) {
                    is Resource.Error -> {
                        progress.dismiss()
                        showToast(it.message.toString())
                    }
                    is Resource.Loading -> progress.show()
                    is Resource.Success -> {
                        progress.dismiss()
                        navigateToHomeScreen()
                    }
                }
            }
        }
    }

    override fun navigateToRegisterScreen() {
        navController?.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    override fun navigateToHomeScreen() {

    }

}