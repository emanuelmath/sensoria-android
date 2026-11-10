package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.repositories.AlertRepository
import javax.inject.Inject

class ResolveAlertUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(alertId: Int): Result<Unit> {
        return repository.resolveAlert(alertId)
    }
}