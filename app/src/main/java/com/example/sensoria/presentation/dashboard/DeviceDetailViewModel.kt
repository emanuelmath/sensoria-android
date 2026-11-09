package com.example.sensoria.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.models.TelemetryReading
import com.example.sensoria.domain.usecases.GetDeviceTelemetryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val getDeviceTelemetryUseCase: GetDeviceTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeviceDetailUiState>(DeviceDetailUiState.Loading)
    val uiState: StateFlow<DeviceDetailUiState> = _uiState.asStateFlow()

    fun loadTelemetry(deviceId: String) {
        val current = _uiState.value
        _uiState.value = if (current is DeviceDetailUiState.Success) {
            current.copy(isRefreshing = true)
        } else {
            DeviceDetailUiState.Loading
        }

        viewModelScope.launch {
            getDeviceTelemetryUseCase(deviceId)
                .onSuccess { response ->
                    _uiState.value = DeviceDetailUiState.Success(
                        deviceName = response.deviceName,
                        readings = response.readings.reversed()
                    )
                }
                .onFailure { error ->
                    _uiState.value = DeviceDetailUiState.Error(
                        message = error.message ?: "No se pudo obtener los datos de telemetría."
                    )
                }
        }
    }
}

