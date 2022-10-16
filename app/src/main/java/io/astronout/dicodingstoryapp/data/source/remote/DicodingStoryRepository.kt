package io.astronout.dicodingstoryapp.data.source.remote

import io.astronout.dicodingstoryapp.data.source.remote.model.AddNewStoryResponse
import io.astronout.dicodingstoryapp.data.source.remote.model.StoriesResponse
import io.astronout.dicodingstoryapp.domain.model.Login
import io.astronout.dicodingstoryapp.domain.model.Register
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DicodingStoryRepository {

    fun login(email: String, password: String): Flow<Resource<Login>>

    fun register(name: String, email: String, password: String): Flow<Resource<Register>>

    fun getAllStories(): Flow<Resource<StoriesResponse>>

    fun addNewStory(file: File, description: String): Flow<Resource<AddNewStoryResponse>>

}
