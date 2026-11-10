package com.example.sensoria.presentation.alerts

import com.example.sensoria.domain.models.Alert

sealed class AlertsUiState {
    object Loading : AlertsUiState()
    data class Success(val alerts: List<Alert>) : AlertsUiState()
    data class Error(val message: String) : AlertsUiState()
}