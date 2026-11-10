package com.example.sensoria.presentation.profile

import com.example.sensoria.domain.models.Company

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(
        val userName: String,
        val email: String,
        val currentCompany: String,
        val userRole: String,
        val availableCompanies: List<Company> = emptyList(),
        val numberOfCompanies: Int
    ) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}