package com.example.sensoria.presentation.support.create

sealed class CreateTicketUiState {
    object Idle : CreateTicketUiState()
    object Loading : CreateTicketUiState()
    data class Success(val newTicketId: Int) : CreateTicketUiState()
    data class Error(val message: String) : CreateTicketUiState()
}