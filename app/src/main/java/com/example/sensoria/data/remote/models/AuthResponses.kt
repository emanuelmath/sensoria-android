package com.example.sensoria.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val companies: List<CompanyDto> = emptyList()
)

@Serializable
data class CompanyDto(
    val id: Int,
    val name: String? = null,
    val role: String
)

@Serializable
data class CompanyListResponse(val data: List<CompanyDto>)