package io.astronout.dicodingstoryapp.data.source.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddNewStoryResponse(
    @Json(name = "error")
    val error: Boolean? = null,
    @Json(name = "message")
    val message: String? = null
)