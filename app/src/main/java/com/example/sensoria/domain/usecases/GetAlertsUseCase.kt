package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.Alert
import com.example.sensoria.domain.repositories.AlertRepository
import javax.inject.Inject

class GetAlertsUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(companyId: String): Result<List<Alert>> {
        return repository.getAlerts(companyId)
    }
}