package com.example.sensoria.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sensoria_auth_prefs")

class AuthPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("sanctum_token")
        private val COMPANY_ID_KEY = stringPreferencesKey("current_company_id")
        private val USER_NAME_KEY = stringPreferencesKey("current_user_name")
        private val COMPANY_NAME_KEY = stringPreferencesKey("current_company_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("current_user_email")
        private val CURRENT_ROLE_KEY = stringPreferencesKey("current_user_role")
        private val CURRENT_NUMBER_OF_COMPANIES = intPreferencesKey("current_number_of_companies")
    }

    val getAuthToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val getCurrentCompanyId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[COMPANY_ID_KEY]
    }

    val getUserName: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: "Usuario"
    }

    val getCompanyName: Flow<String> = dataStore.data.map { preferences ->
        preferences[COMPANY_NAME_KEY] ?: "Workspace"
    }

    val getUserEmail: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY] ?: "Correo no disponible"
    }

    val getCurrentRole: Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENT_ROLE_KEY] ?: "USUARIO"
    }

    val getCurrentNumberOfCompanies: Flow<Int> = dataStore.data.map {
        preferences -> preferences[CURRENT_NUMBER_OF_COMPANIES] ?: 0
    }

    suspend fun saveAuthData(token: String, companyId: String, userName: String, email: String, companyName: String, role: String, numberOfCompanies: Int) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[COMPANY_ID_KEY] = companyId
            preferences[USER_NAME_KEY] = userName
            preferences[USER_EMAIL_KEY] = email
            preferences[COMPANY_NAME_KEY] = companyName
            preferences[CURRENT_ROLE_KEY] = role
            preferences[CURRENT_NUMBER_OF_COMPANIES] = numberOfCompanies
        }
    }

    suspend fun saveNumberOfCompanies(numberOfCompanies: Int) {
        dataStore.edit { preferences ->
            preferences[CURRENT_NUMBER_OF_COMPANIES] = numberOfCompanies
        }
    }

    suspend fun saveCompanySelection(id: String, name: String, role: String, numberOfCompanies: Int) {
        dataStore.edit { preferences ->
            preferences[COMPANY_ID_KEY] = id
            preferences[COMPANY_NAME_KEY] = name
            preferences[CURRENT_ROLE_KEY] = role
            preferences[CURRENT_NUMBER_OF_COMPANIES] = numberOfCompanies
        }
    }
    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}