package io.astronout.dicodingstoryapp.utils

import io.astronout.dicodingstoryapp.data.source.remote.model.StoriesResponse
import io.astronout.dicodingstoryapp.domain.model.Login
import io.astronout.dicodingstoryapp.domain.model.Register
import io.astronout.dicodingstoryapp.domain.model.Story

object Dummies {
    fun generateDummyStoriesResponse(): StoriesResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoriesResponse.StoryResponse>()

        for (i in 0 until 10) {
            listStory.add(
                StoriesResponse.StoryResponse(
                    id = "story-frySJeBzI3-V62IH",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670091088361_wadcb_2J.jpg",
                    createdAt = "2022-12-03T17:59:14.076Z",
                    name = "dicoding",
                    description = "Lorem Ipsum",
                    lon = -7.792944,
                    lat = 110.4077913
                )
            )
        }

        return StoriesResponse(error, listStory, message)
    }

    fun generateDummyListStory(): List<Story> {
        val items = mutableListOf<Story>()

        for (i in 0 until 10) {
            items.add(
                Story(
                    id = "story-frySJeBzI3-V62IH",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670091088361_wadcb_2J.jpg",
                    createdAt = "2022-12-03T17:59:14.076Z",
                    name = "dicoding",
                    description = "Lorem Ipsum",
                    lon = -7.792944,
                    lat = 110.4077913
                )
            )
        }
        return items
    }


    fun generateDummyLogin(): Login {
        return Login(
            loginResult = Login.LoginResult(
                userId = "user-IhHXJaEBwR8skkbz",
                name = "John Doe",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUloSFhKYUVCd1I4c2trYnoiLCJpYXQiOjE2NzAwOTIyNDF9.X7VGmWlxs_TY9Asf0OzsBrPbgmth_8yxPZ89CHEK5HQ"
            ),
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegister(): Register {
        return Register(
            error = false,
            message = "success"
        )
    }
}