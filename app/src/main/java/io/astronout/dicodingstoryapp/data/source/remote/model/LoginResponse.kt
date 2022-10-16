package io.astronout.dicodingstoryapp.data.source.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "error")
    val error: Boolean? = null,
    @Json(name = "loginResult")
    val loginResult: LoginResult? = null,
    @Json(name = "message")
    val message: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class LoginResult(
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "token")
        val token: String? = null,
        @Json(name = "userId")
        val userId: String? = null
    )
}