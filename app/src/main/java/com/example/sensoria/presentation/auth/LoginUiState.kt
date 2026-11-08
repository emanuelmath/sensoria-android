package com.example.sensoria.presentation.auth

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val companyId: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}