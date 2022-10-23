package io.astronout.dicodingstoryapp.ui.auth.register

interface RegisterContract {
    fun doRegister(name: String, email: String, password: String)
    fun navigateToLoginScreen()
}