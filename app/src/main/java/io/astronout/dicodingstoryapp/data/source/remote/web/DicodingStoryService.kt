package io.astronout.dicodingstoryapp.data.source.remote.web

import com.skydoves.sandwich.ApiResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.AddNewStoryResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.LoginResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.RegisterResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DicodingStoryService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): ApiResponse<StoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): ApiResponse<AddNewStoryResponse>

}