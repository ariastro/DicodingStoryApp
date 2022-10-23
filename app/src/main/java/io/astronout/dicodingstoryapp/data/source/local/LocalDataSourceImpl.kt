package io.astronout.dicodingstoryapp.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dataStore: DataStore<Preferences>): LocalDataSource {

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

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}