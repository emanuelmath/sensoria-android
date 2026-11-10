package com.example.sensoria.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeUserData()
    }

    private fun observeUserData() {
        viewModelScope.launch {
            combine(
                authPreferences.getUserName,
                authPreferences.getCompanyName,
                authPreferences.getUserEmail,
                authPreferences.getCurrentRole,
                authPreferences.getCurrentNumberOfCompanies

            ) { name, company, email, role, numberOfCompanies ->
                ProfileUiState.Success(
                    userName = name,
                    email = email,
                    currentCompany = company,
                    userRole = role,
                    numberOfCompanies = numberOfCompanies
                )
            }.catch { e ->
                _uiState.value = ProfileUiState.Error(e.message ?: "Error al cargar el perfil")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                authRepository.logout()
                authPreferences.clearAuthData()
                onLogoutComplete()
            } catch (e: Exception) {
                authPreferences.clearAuthData()
                onLogoutComplete()
            }
        }
    }
}