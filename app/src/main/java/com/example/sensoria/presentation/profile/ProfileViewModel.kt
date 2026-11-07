package com.example.sensoria.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensoria.data.local.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authPreferences: AuthPreferences
) : ViewModel() {

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            authPreferences.clearAuthData()
            onLogoutComplete()
        }
    }
}