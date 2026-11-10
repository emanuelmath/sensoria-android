package com.example.sensoria.presentation.auth

import com.example.sensoria.domain.models.Company

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val companyId: String, val message: String) : LoginUiState()
    data class RequireCompanySelection(val companies: List<Company>) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}