package io.astronout.dicodingstoryapp.ui.auth.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authUsecase: AuthUsecase) : ViewModel() {

    fun register(name: String, email: String, password: String) = authUsecase.register(name, email, password)

}