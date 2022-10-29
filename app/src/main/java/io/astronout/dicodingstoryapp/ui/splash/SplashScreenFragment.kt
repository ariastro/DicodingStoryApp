package io.astronout.dicodingstoryapp.ui.splash

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.collectLifecycleFlow
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment(R.layout.fragment_splash_screen) {

    private val viewModel: SplashScreenViewModel by viewModels()
    private val navController: NavController? by lazy { findNavController() }

    override fun initObserver() {
        super.initObserver()
        collectLifecycleFlow(viewModel.getAuthToken()) {
            delay(3000)
            if (it.isNotEmpty()) {
                navController?.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
            } else {
                navController?.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
            }
        }
    }

}