package com.example.sensoria.presentation.support.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.usecases.GetTicketDetailUseCase
import com.example.sensoria.domain.usecases.ReplyTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    private val getTicketDetailUseCase: GetTicketDetailUseCase,
    private val replyTicketUseCase: ReplyTicketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketDetailUiState>(TicketDetailUiState.Loading)
    val uiState: StateFlow<TicketDetailUiState> = _uiState.asStateFlow()

    private var currentTicketId: Int = -1

    fun loadTicket(ticketId: Int) {
        currentTicketId = ticketId
        _uiState.value = TicketDetailUiState.Loading

        viewModelScope.launch {
            getTicketDetailUseCase(ticketId).fold(
                onSuccess = { detail ->
                    _uiState.value = TicketDetailUiState.Success(
                        ticketSubject = detail.ticket.subject,
                        ticketStatus = detail.ticket.status,
                        messages = detail.messages
                    )
                },
                onFailure = { error ->
                    _uiState.value = TicketDetailUiState.Error(
                        error.message ?: "No se pudo cargar el ticket."
                    )
                }
            )
        }
    }

    fun sendMessage(messageText: String) {
        if (messageText.isBlank() || currentTicketId == -1) return

        val currentState = _uiState.value
        if (currentState is TicketDetailUiState.Success) {
            _uiState.value = currentState.copy(isSending = true)

            viewModelScope.launch {
                replyTicketUseCase(currentTicketId, messageText).fold(
                    onSuccess = { newMessage ->
                        val updatedMessages = currentState.messages + newMessage
                        _uiState.value = currentState.copy(
                            messages = updatedMessages,
                            isSending = false
                        )
                    },
                    onFailure = {
                        _uiState.value = currentState.copy(isSending = false)
                    }
                )
            }
        }
    }
}