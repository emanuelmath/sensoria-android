package com.example.sensoria.presentation.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.usecases.GetTicketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SupportViewModel @Inject constructor(
    private val getTicketsUseCase: GetTicketsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SupportUiState>(SupportUiState.Loading)
    val uiState: StateFlow<SupportUiState> = _uiState.asStateFlow()

    init {
        loadTickets()
    }

    fun loadTickets() {
        val current = _uiState.value
        _uiState.value = if (current is SupportUiState.Success) {
            current.copy(isRefreshing = true)
        } else {
            SupportUiState.Loading
        }

        viewModelScope.launch {
            getTicketsUseCase()
                .onSuccess { list ->
                    _uiState.value = SupportUiState.Success(tickets = list)
                }
                .onFailure { error ->
                    _uiState.value = SupportUiState.Error(
                        message = error.message ?: "No se pudieron obtener los tickets."
                    )
                }
        }
    }
}