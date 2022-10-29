package io.astronout.dicodingstoryapp.data.source.remote

import io.astronout.dicodingstoryapp.data.source.local.LocalDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val localDataSource: LocalDataSource) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            delay(1000)
            localDataSource.getAuthToken().first()
        }
        Timber.d("checkToken: $token")
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}