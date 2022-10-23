package io.astronout.dicodingstoryapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authUsecase: AuthUsecase) : ViewModel() {

    fun login(email: String, password: String) = authUsecase.login(email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authUsecase.saveAuthToken(token)
        }
    }
}