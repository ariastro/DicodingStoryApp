package io.astronout.dicodingstoryapp.data.source.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer " + "token")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}