package io.astronout.dicodingstoryapp.data.source.local

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAuthToken(): Flow<String>

    suspend fun saveAuthToken(token: String)

}