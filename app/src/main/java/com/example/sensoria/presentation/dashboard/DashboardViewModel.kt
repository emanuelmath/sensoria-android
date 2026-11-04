package com.example.sensoria.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.models.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadDevicesForCompany(companyId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Simular que vienen datos de la red.
            delay(1.seconds)

            // Respuesta simulada.
            val fakeDevices = listOf(
                Device("dev-1", "Cámara Principal", "Sensor de Movimiento", true, "Activo hace 2m"),
                Device("dev-2", "Refrigerador #3", "Sensor de Temperatura", true, "4°C"),
                Device("dev-3", "Puerta Trasera", "Hub Central", false, "Desconectado")
            )

            _devices.value = fakeDevices
            _isLoading.value = false
        }
    }
}