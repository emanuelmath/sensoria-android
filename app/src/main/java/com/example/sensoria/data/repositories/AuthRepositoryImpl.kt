package com.example.sensoria.data.repositories

import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.data.remote.AuthApi
import com.example.sensoria.data.remote.LoginRequest
import com.example.sensoria.domain.models.Company
import com.example.sensoria.domain.models.User
import com.example.sensoria.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val preferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val request = LoginRequest(email, password)
            val response = api.login(request)

            val companiesDomain = response.user.companies.map { dto ->
                Company(
                    id = dto.id,
                    name = dto.name,
                    role = "PENDIENTE"
                )
            }

            val defaultCompanyId = companiesDomain.firstOrNull()?.id

            val userDomain = User(
                id = response.user.id,
                name = response.user.name,
                email = response.user.email,
                companies = companiesDomain,
                currentCompanyId = defaultCompanyId
            )

            if (defaultCompanyId != null) {
                preferences.saveAuthData(token = response.token, companyId = defaultCompanyId)
            } else {
                preferences.saveAuthData(token = response.token, companyId = "")
            }

            Result.success(userDomain)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}