package com.example.sensoria.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val companies: List<CompanyDto> = emptyList()
)

@Serializable
data class CompanyDto(
    val id: String,
    val name: String? = null
)