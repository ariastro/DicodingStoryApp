package io.astronout.dicodingstoryapp.domain

import io.astronout.dicodingstoryapp.data.source.DicodingStoryRepository
import io.astronout.dicodingstoryapp.domain.model.Login
import io.astronout.dicodingstoryapp.domain.model.Register
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val repo: DicodingStoryRepository) : AuthUsecase {

    override fun login(email: String, password: String): Flow<Resource<Login>> {
        return repo.login(email, password)
    }

    override fun register(name: String, email: String, password: String): Flow<Resource<Register>> {
        return repo.register(name, email, password)
    }

    override suspend fun saveAuthToken(token: String) {
        repo.saveAuthToken(token)
    }

    override fun getAuthToken(): Flow<String> = repo.getAuthToken()

}