package com.example.sensoria.domain.repositories

import com.example.sensoria.domain.models.Company
import com.example.sensoria.domain.models.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getMyCompanies(): Result<List<Company>>
}