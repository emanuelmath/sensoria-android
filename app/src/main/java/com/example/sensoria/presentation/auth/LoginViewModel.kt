package com.example.sensoria.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = loginUseCase(email.trim(), password)

            result.fold(
                onSuccess = { user ->
                    val companyId = user.currentCompanyId ?: "sin-empresa"
                    _uiState.value = LoginUiState.Success(companyId)
                },
                onFailure = { exception ->
                    _uiState.value = LoginUiState.Error(exception.message ?: "Error de red desconocido.")
                }
            )
        }
    }
}