package io.astronout.dicodingstoryapp.data.source.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.astronout.dicodingstoryapp.data.source.local.entity.StoryEntity
import io.astronout.dicodingstoryapp.domain.model.Story

@JsonClass(generateAdapter = true)
data class StoriesResponse(
    @Json(name = "error")
    val error: Boolean? = null,
    @Json(name = "listStory")
    val listStory: List<StoryResponse>? = null,
    @Json(name = "message")
    val message: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class StoryResponse(
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
    ) {
        fun toStory(): Story {
            return Story(
                createdAt = createdAt.orEmpty(),
                description = description.orEmpty(),
                id = id.orEmpty(),
                lat = lat ?: 0.0,
                lon = lon ?: 0.0,
                name = name.orEmpty(),
                photoUrl = photoUrl.orEmpty()
            )
        }
        fun toStoryEntity(): StoryEntity {
            return StoryEntity(
                id = id.orEmpty(),
                name = name.orEmpty(),
                description = description.orEmpty(),
                createdAt = createdAt.orEmpty(),
                photoUrl = photoUrl.orEmpty(),
                lat = lat ?: 0.0,
                lon = lon ?: 0.0
            )
        }
    }
}