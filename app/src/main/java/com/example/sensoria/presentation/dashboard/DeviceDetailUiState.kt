package com.example.sensoria.presentation.dashboard

import com.example.sensoria.domain.models.TelemetryReading


sealed class DeviceDetailUiState {
    object Loading : DeviceDetailUiState()

    data class Success(
        val deviceName: String,
        val readings: List<TelemetryReading>,
        val isRefreshing: Boolean = false
    ) : DeviceDetailUiState()

    data class Error(val message: String) : DeviceDetailUiState()
}
