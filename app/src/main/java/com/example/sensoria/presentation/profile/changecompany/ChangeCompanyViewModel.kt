package com.example.sensoria.presentation.profile.changecompany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.domain.models.Company
import com.example.sensoria.domain.usecases.GetMyCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeCompanyViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val getMyCompaniesUseCase: GetMyCompaniesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChangeCompanyUiState>(ChangeCompanyUiState.Loading)
    val uiState: StateFlow<ChangeCompanyUiState> = _uiState.asStateFlow()
    private val _numberOfCompanies = MutableStateFlow<Int>(0)

    init {
        loadCompanies()
    }

    fun loadCompanies() {
        viewModelScope.launch {
            _uiState.value = ChangeCompanyUiState.Loading
            try {
                val currentId = authPreferences.getCurrentCompanyId.first() ?: ""

                getMyCompaniesUseCase().fold(
                    onSuccess = { list ->
                        _uiState.value = ChangeCompanyUiState.Success(list, currentId)
                        _numberOfCompanies.value = list.size
                    },
                    onFailure = { error ->
                        _uiState.value = ChangeCompanyUiState.Error(error.message ?: "Ocurrió un error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ChangeCompanyUiState.Error("Hubo un problema al cargar empresas.")
            }
        }
    }

    fun selectCompany(company: Company, onSelectionComplete: () -> Unit) {
        viewModelScope.launch {
            authPreferences.saveCompanySelection(company.id, company.name, company.role, _numberOfCompanies.value)
            onSelectionComplete()
        }
    }
}