package io.astronout.dicodingstoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.astronout.dicodingstoryapp.BuildConfig
import io.astronout.dicodingstoryapp.data.source.DicodingStoryDataStore
import io.astronout.dicodingstoryapp.data.source.DicodingStoryRepository
import io.astronout.dicodingstoryapp.data.source.local.LocalDataSource
import io.astronout.dicodingstoryapp.data.source.local.LocalDataSourceImpl
import io.astronout.dicodingstoryapp.data.source.remote.AuthInterceptor
import io.astronout.dicodingstoryapp.data.source.remote.web.DicodingStoryApi
import io.astronout.dicodingstoryapp.data.source.remote.web.DicodingStoryService
import io.astronout.dicodingstoryapp.domain.AuthInteractor
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import io.astronout.dicodingstoryapp.domain.StoryInteractor
import io.astronout.dicodingstoryapp.domain.StoryUsecase
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

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credentials")

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(dataStore: DataStore<Preferences>): LocalDataSource = LocalDataSourceImpl(dataStore)

    @Provides
    @Singleton
    fun provideOkHttpClient(localDataSource: LocalDataSource) = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(AuthInterceptor(localDataSource))
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(localDataSource))
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
    fun provideDicodingRepository(dicodingStoryApi: DicodingStoryApi, localDataSource: LocalDataSource, ioDispatcher: CoroutineDispatcher): DicodingStoryRepository {
        return DicodingStoryDataStore(dicodingStoryApi, localDataSource, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAuthUsecase(repo: DicodingStoryRepository): AuthUsecase {
        return AuthInteractor(repo)
    }

    @Provides
    @Singleton
    fun provideStoryUsecase(repo: DicodingStoryRepository): StoryUsecase {
        return StoryInteractor(repo)
    }

}