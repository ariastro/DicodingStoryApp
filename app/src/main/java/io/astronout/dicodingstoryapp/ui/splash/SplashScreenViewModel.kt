package io.astronout.dicodingstoryapp.ui.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val authUsecase: AuthUsecase) : ViewModel() {

    fun getAuthToken() = authUsecase.getAuthToken()

}