package io.astronout.dicodingstoryapp.data.source.local

import androidx.paging.PagingSource
import io.astronout.dicodingstoryapp.data.source.local.entity.RemoteKeys
import io.astronout.dicodingstoryapp.data.source.local.entity.StoryEntity
import io.astronout.dicodingstoryapp.data.source.local.room.DicodingStoryDatabase
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAuthToken(): Flow<String>

    suspend fun saveAuthToken(token: String)

    fun getDatabase(): DicodingStoryDatabase
    fun getAllStories(): PagingSource<Int, StoryEntity>
    suspend fun insertStories(stories: List<StoryEntity>)
    suspend fun clearStories()
    suspend fun getRemoteKeys(id: String): RemoteKeys?
    suspend fun insertRemoteKeys(keys: List<RemoteKeys>)
    suspend fun clearRemoteKeys()

}