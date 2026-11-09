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

    private val _telemetry = MutableStateFlow<List<TelemetryReading>>(emptyList())
    val telemetry: StateFlow<List<TelemetryReading>> = _telemetry.asStateFlow()

    private val _deviceName = MutableStateFlow("Cargando...")
    val deviceName: StateFlow<String> = _deviceName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTelemetry(deviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _deviceName.value = "Cargando..."

            val result = getDeviceTelemetryUseCase(deviceId)

            result.onSuccess { response ->
                _deviceName.value = response.deviceName
                _telemetry.value = response.readings.reversed()
            }.onFailure {
                _deviceName.value = "Error de conexión"
            }

            _isLoading.value = false
        }
    }
}