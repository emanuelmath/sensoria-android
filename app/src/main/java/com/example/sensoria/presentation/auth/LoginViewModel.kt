package com.example.sensoria.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.models.Company
import com.example.sensoria.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = repository.login(email, password)

            result.onSuccess { user ->
                when {
                    user.companies.isEmpty() -> {
                        _uiState.value = LoginUiState.Error("No tienes un espacio de trabajo asignado.")
                    }
                    user.companies.size == 1 -> {
                        _uiState.value = LoginUiState.Success(user.companies.first().id)
                    }
                    else -> {
                        _uiState.value = LoginUiState.RequireCompanySelection(user.companies)
                    }
                }
            }.onFailure { error ->
                _uiState.value = LoginUiState.Error(error.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}