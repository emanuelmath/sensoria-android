package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.User
import com.example.sensoria.domain.repositories.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()

    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("El correo y la contraseña son obligatorios."))
        }

        if (!email.matches(emailRegex)) {
            return Result.failure(Exception("Ingresa un correo electrónico corporativo válido."))
        }

        return repository.login(email, password)
    }
}