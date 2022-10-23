package io.astronout.dicodingstoryapp.ui.auth.login

interface LoginContract {
    fun doLogin(email: String, password: String)
    fun navigateToRegisterScreen()
    fun navigateToHomeScreen()
}