package io.astronout.dicodingstoryapp.data.source.remote

import io.astronout.dicodingstoryapp.data.source.remote.model.AddNewStoryResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.StoriesResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.LoginResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.RegisterResponse
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DicodingStoryRepository {

    fun login(email: String, password: String): Flow<Resource<LoginResponse>>

    fun register(name: String, email: String, password: String): Flow<Resource<RegisterResponse>>

    fun getAllStories(): Flow<Resource<StoriesResponse>>

    fun addNewStory(file: File, description: String): Flow<Resource<AddNewStoryResponse>>

}
