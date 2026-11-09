package com.example.sensoria.presentation.support

import com.example.sensoria.domain.models.Ticket

sealed class SupportUiState {
    object Loading : SupportUiState()
    data class Success(val tickets: List<Ticket>, val isRefreshing: Boolean = false) : SupportUiState()
    data class Error(val message: String) : SupportUiState()
}