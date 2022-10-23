package io.astronout.dicodingstoryapp.ui.auth.register

import android.viewbinding.library.fragment.viewBinding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentRegisterBinding
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.showToast
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment(R.layout.fragment_register), RegisterContract {

    private val binding: FragmentRegisterBinding by viewBinding()
    private val viewModel: RegisterViewModel by viewModels()
    private val navController: NavController? by lazy { findNavController() }

    override fun initAction() {
        super.initAction()
        with(binding) {
            tvLogin.setOnClickListener {
                navigateToLoginScreen()
            }
            etFullName.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) etFullName.error = getString(R.string.error_fullname_not_valid)
            }
            btnRegister.setOnClickListener {
                val name = etFullName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                when {
                    !etFullName.error.isNullOrEmpty() -> etFullName.requestFocus()
                    !etEmail.isValid -> etEmail.requestFocus()
                    !etPassword.isValid -> etPassword.requestFocus()
                    name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() -> doRegister(name, email, password)
                }
            }
        }
    }

    override fun doRegister(name: String, email: String, password: String) {
        lifecycleScope.launch {
            viewModel.register(name, email, password).collect {
                when(it) {
                    is Resource.Error -> {
                        progress.dismiss()
                        showToast(it.message.toString())
                    }
                    is Resource.Loading -> progress.show()
                    is Resource.Success -> {
                        progress.dismiss()
                        navigateToLoginScreen()
                        showToast(getString(R.string.label_register_success))
                    }
                }
            }
        }
    }

    override fun navigateToLoginScreen() {
        navController?.navigateUp()
    }

}