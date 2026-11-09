package com.example.sensoria.presentation.support.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.usecases.CreateTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val createTicketUseCase: CreateTicketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateTicketUiState>(CreateTicketUiState.Idle)
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    fun createTicket(subject: String, category: String, priority: String, description: String) {
        if (subject.isBlank() || description.isBlank()) {
            _uiState.value = CreateTicketUiState.Error("El asunto y la descripción son obligatorios.")
            return
        }

        _uiState.value = CreateTicketUiState.Loading

        viewModelScope.launch {
            createTicketUseCase(subject, category, priority, description).fold(
                onSuccess = { ticketId ->
                    _uiState.value = CreateTicketUiState.Success(ticketId)
                },
                onFailure = { error ->
                    _uiState.value = CreateTicketUiState.Error(
                        error.message ?: "Ocurrió un error al crear el ticket."
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = CreateTicketUiState.Idle
    }
}