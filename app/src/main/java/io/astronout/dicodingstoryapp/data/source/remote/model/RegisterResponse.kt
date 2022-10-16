package io.astronout.dicodingstoryapp.data.source.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.astronout.dicodingstoryapp.domain.model.Register

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "error")
    val error: Boolean? = null,
    @Json(name = "message")
    val message: String? = null
) {
    fun toRegister(): Register {
        return Register(
            error = error ?: false,
            message = message.orEmpty()
        )
    }
}