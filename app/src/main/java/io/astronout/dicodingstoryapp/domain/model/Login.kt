package io.astronout.dicodingstoryapp.domain.model

data class Login(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
) {
    data class LoginResult(
        val name: String,
        val token: String,
        val userId: String
    )
}