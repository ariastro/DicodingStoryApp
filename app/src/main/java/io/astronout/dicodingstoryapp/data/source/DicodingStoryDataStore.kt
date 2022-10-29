package io.astronout.dicodingstoryapp.data.source

import com.skydoves.sandwich.map
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.astronout.dicodingstoryapp.data.source.local.LocalDataSource
import io.astronout.dicodingstoryapp.data.source.remote.ErrorResponseMapper
import io.astronout.dicodingstoryapp.data.source.remote.web.DicodingStoryApi
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class DicodingStoryDataStore @Inject constructor(
    private val api: DicodingStoryApi,
    private val localDataSource: LocalDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : DicodingStoryRepository {

    override fun login(email: String, password: String) = flow {
        api.login(email, password).let {
            it.suspendOnSuccess {
                emit(Resource.Success(data.toLogin()))
            }.suspendOnError {
                emit(Resource.Error(map(ErrorResponseMapper)?.message.orEmpty()))
            }.suspendOnException {
                emit(Resource.Error(message.orEmpty()))
            }
        }
    }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)

    override fun register(
        name: String,
        email: String,
        password: String
    ) = flow {
        api.register(name, email, password).let {
            it.suspendOnSuccess {
                emit(Resource.Success(data.toRegister()))
            }.suspendOnError {
                emit(Resource.Error(map(ErrorResponseMapper)?.message.orEmpty()))
            }.suspendOnException {
                emit(Resource.Error(message.orEmpty()))
            }
        }
    }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)

    override fun getAllStories() = flow {
        api.getAllStories().let {
            it.suspendOnSuccess {
                emit(Resource.Success(data.listStory?.map { data ->
                    data.toStory()
                }.orEmpty()))
            }.suspendOnError {
                emit(Resource.Error(map(ErrorResponseMapper)?.message.orEmpty()))
            }.suspendOnException {
                emit(Resource.Error(message.orEmpty()))
            }
        }
    }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)

    override fun addNewStory(file: File, description: String) = flow {
        val requestImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData("photo", file.name, requestImage)
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        api.addNewStory(imageMultipart, requestDescription).let {
            it.suspendOnSuccess {
                emit(Resource.Success(data))
            }.suspendOnError {
                emit(Resource.Error(map(ErrorResponseMapper)?.message.orEmpty()))
            }.suspendOnException {
                emit(Resource.Error(message.orEmpty()))
            }
        }
    }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)

    override fun getAuthToken(): Flow<String> = localDataSource.getAuthToken()

    override suspend fun saveAuthToken(token: String) {
        localDataSource.saveAuthToken(token)
    }

}