package io.astronout.dicodingstoryapp.data.source.remote.web

import com.skydoves.sandwich.ApiResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.AddNewStoryResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.StoriesResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.LoginResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DicodingStoryApi @Inject constructor(private val dicodingStoryService: DicodingStoryService): DicodingStoryService {

    override suspend fun login(email: String, password: String): ApiResponse<LoginResponse> {
        return dicodingStoryService.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): ApiResponse<RegisterResponse> {
        return dicodingStoryService.register(name, email, password)
    }

    override suspend fun getAllStories(page: Int?, size: Int?): ApiResponse<StoriesResponse> {
        return dicodingStoryService.getAllStories(page, size)
    }

    override suspend fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): ApiResponse<AddNewStoryResponse> {
        return dicodingStoryService.addNewStory(file, description)
    }

}