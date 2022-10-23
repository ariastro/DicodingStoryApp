package io.astronout.dicodingstoryapp.di

import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.astronout.dicodingstoryapp.BuildConfig
import io.astronout.dicodingstoryapp.data.source.remote.AuthInterceptor
import io.astronout.dicodingstoryapp.data.source.remote.DicodingStoryDataStore
import io.astronout.dicodingstoryapp.data.source.remote.DicodingStoryRepository
import io.astronout.dicodingstoryapp.data.source.remote.web.DicodingStoryApi
import io.astronout.dicodingstoryapp.data.source.remote.web.DicodingStoryService
import io.astronout.dicodingstoryapp.domain.AuthInteractor
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(AuthInterceptor())
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDicodingStoryService(retrofit: Retrofit): DicodingStoryService =
        retrofit.create(DicodingStoryService::class.java)

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideDicodingRepository(dicodingStoryApi: DicodingStoryApi, ioDispatcher: CoroutineDispatcher): DicodingStoryRepository {
        return DicodingStoryDataStore(dicodingStoryApi, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAuthUsecase(repo: DicodingStoryRepository): AuthUsecase {
        return AuthInteractor(repo)
    }

}