package com.example.store.data.stored

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.store.utils.SESSION_AUTH_DATA
import com.example.store.utils.USER_TOKEN_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val appContext = context.applicationContext

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            SESSION_AUTH_DATA
        )
        private val USER_TOKEN = stringPreferencesKey(USER_TOKEN_DATA)
    }

    suspend fun saveToken(token: String) {
        appContext.dataStore.edit {
            it[USER_TOKEN] = token
        }
    }

    val getToken: Flow<String?> = appContext.dataStore.data.map {
        it[USER_TOKEN]
    }

    suspend fun clearToken(){
        appContext.dataStore.edit {
            it.clear()
        }
    }
}