package com.example.sensoria.presentation.support.detail

import com.example.sensoria.domain.models.TicketMessage

sealed class TicketDetailUiState {
    object Loading : TicketDetailUiState()

    data class Success(
        val ticketSubject: String,
        val ticketStatus: String,
        val messages: List<TicketMessage>,
        val isSending: Boolean = false
    ) : TicketDetailUiState()

    data class Error(val message: String) : TicketDetailUiState()
}