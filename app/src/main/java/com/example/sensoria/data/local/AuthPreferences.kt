package com.example.sensoria.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sensoria_auth_prefs")

class AuthPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("sanctum_token")
        private val COMPANY_ID_KEY = stringPreferencesKey("current_company_id")
    }

    val getAuthToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val getCurrentCompanyId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[COMPANY_ID_KEY]
    }

    suspend fun saveAuthData(token: String, companyId: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[COMPANY_ID_KEY] = companyId
        }
    }

    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}