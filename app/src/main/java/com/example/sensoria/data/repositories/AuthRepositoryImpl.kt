package com.example.sensoria.data.repositories

import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.data.remote.AuthApi
import com.example.sensoria.data.remote.models.LoginRequest
import com.example.sensoria.domain.models.Company
import com.example.sensoria.domain.models.User
import com.example.sensoria.domain.repositories.AuthRepository
import retrofit2.HttpException
import kotlinx.serialization.SerializationException
import java.io.IOException

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
                    name = dto.name ?: "Workspace sin nombre",
                    role = "PENDIENTE"
                )
            }
            val userDomain: User

            if(companiesDomain.size <= 1) {
                val defaultCompany = companiesDomain.firstOrNull()
                val defaultCompanyId = defaultCompany?.id
                val defaultCompanyName = defaultCompany?.name ?: "Sin Empresa"

                userDomain = User(
                    id = response.user.id,
                    name = response.user.name,
                    email = response.user.email,
                    companies = companiesDomain,
                    currentCompanyId = defaultCompanyId
                )

                preferences.saveAuthData(
                    token = response.token,
                    companyId = defaultCompanyId ?: "",
                    userName = userDomain.name,
                    companyName = defaultCompanyName
                )
            } else {
                userDomain = User(
                    id = response.user.id,
                    name = response.user.name,
                    email = response.user.email,
                    companies = companiesDomain,
                    currentCompanyId = ""
                )

                preferences.saveAuthData(
                    token = response.token,
                    companyId = "",
                    userName = userDomain.name,
                    companyName = ""
                )
            }

            Result.success(userDomain)

        } catch (e: Exception) {
            val friendlyMessage = when (e) {
                is HttpException -> {
                    if (e.code() == 401) "Credenciales incorrectas."
                    else "Ocurrió un problema en el servidor. Intente más tarde."
                }
                is IOException -> "No hay conexión al servidor. Revisa tu red."
                is SerializationException -> "Error de procesamiento interno de datos."
                else -> "Ocurrió un error inesperado. Contacte a soporte."
            }
            Result.failure(Exception(friendlyMessage))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            api.logout()
            preferences.clearAuthData()
            Result.success(Unit)
        } catch (e: Exception) {
            preferences.clearAuthData()
            Result.success(Unit)
        }
    }
}