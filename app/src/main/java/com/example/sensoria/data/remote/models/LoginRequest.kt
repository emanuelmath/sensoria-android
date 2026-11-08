package com.example.sensoria.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)