package com.example.sensoria.data.remote

import com.example.sensoria.data.remote.models.LoginRequest
import com.example.sensoria.data.remote.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("v1/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @POST("v1/logout")
    suspend fun logout()
}