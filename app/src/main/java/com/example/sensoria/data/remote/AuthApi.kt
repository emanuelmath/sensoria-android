package com.example.sensoria.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class LoginRequest(val email: String, val password: String)

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
    val name: String
)

interface AuthApi {
    @POST("api/v1/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}