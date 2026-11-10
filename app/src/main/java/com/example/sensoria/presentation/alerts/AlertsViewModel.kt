package com.example.sensoria.presentation.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.domain.repositories.AlertRepository
import com.example.sensoria.domain.usecases.GetAlertsUseCase
import com.example.sensoria.domain.usecases.ResolveAlertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val getAlertsUseCase: GetAlertsUseCase,
    private val resolveAlertUseCase: ResolveAlertUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlertsUiState>(AlertsUiState.Loading)
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _uiState.value = AlertsUiState.Loading
            val companyId = authPreferences.getCurrentCompanyId.first() ?: return@launch

            getAlertsUseCase(companyId).fold(
                onSuccess = { _uiState.value = AlertsUiState.Success(it) },
                onFailure = { _uiState.value = AlertsUiState.Error(it.message ?: "Error") }
            )
        }
    }

    fun resolveAlert(alertId: Int, onShowToast: (String) -> Unit) {
        viewModelScope.launch {
            resolveAlertUseCase(alertId).fold(
                onSuccess = {
                    onShowToast("Alerta resuelta")
                    loadAlerts()
                },
                onFailure = { onShowToast("Error al resolver") }
            )
        }
    }
}