package com.example.sensoria.domain.repositories

import com.example.sensoria.domain.models.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}