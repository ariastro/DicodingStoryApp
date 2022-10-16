package io.astronout.dicodingstoryapp.data.source.remote.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoriesResponse(
    @Json(name = "error")
    val error: Boolean? = null,
    @Json(name = "listStory")
    val listStory: List<Story?>? = null,
    @Json(name = "message")
    val message: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class Story(
        @Json(name = "createdAt")
        val createdAt: String? = null,
        @Json(name = "description")
        val description: String? = null,
        @Json(name = "id")
        val id: String? = null,
        @Json(name = "lat")
        val lat: Double? = null,
        @Json(name = "lon")
        val lon: Double? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "photoUrl")
        val photoUrl: String? = null
    )
}