package io.astronout.dicodingstoryapp.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.PagingSource
import io.astronout.dicodingstoryapp.data.source.local.entity.RemoteKeys
import io.astronout.dicodingstoryapp.data.source.local.entity.StoryEntity
import io.astronout.dicodingstoryapp.data.source.local.room.DicodingStoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appDatabase: DicodingStoryDatabase
) : LocalDataSource {

    override fun getAuthToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY].orEmpty()
        }
    }

    override suspend fun saveAuthToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    override fun getDatabase(): DicodingStoryDatabase = appDatabase

    override fun getAllStories(): PagingSource<Int, StoryEntity> {
        return appDatabase.storyDao().getAllStories()
    }

    override suspend fun insertStories(stories: List<StoryEntity>) {
        appDatabase.storyDao().insertStory(stories)
    }

    override suspend fun clearStories() {
        appDatabase.storyDao().deleteAllStories()
    }

    override suspend fun getRemoteKeys(id: String): RemoteKeys? {
        return appDatabase.remoteKeysDao().getRemoteKeysId(id)
    }

    override suspend fun insertRemoteKeys(keys: List<RemoteKeys>) {
        appDatabase.remoteKeysDao().insertAll(keys)
    }

    override suspend fun clearRemoteKeys() {
        appDatabase.remoteKeysDao().clearRemoteKeys()
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}