package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.Company
import com.example.sensoria.domain.repositories.AuthRepository
import javax.inject.Inject

class GetMyCompaniesUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<Company>> {
        return repository.getMyCompanies()
    }
}