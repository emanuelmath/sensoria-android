package com.example.sensoria.presentation.profile.changecompany

import com.example.sensoria.domain.models.Company

sealed class ChangeCompanyUiState {
    object Loading : ChangeCompanyUiState()
    data class Success(val companies: List<Company>, val currentCompanyId: String) : ChangeCompanyUiState()
    data class Error(val message: String) : ChangeCompanyUiState()
}