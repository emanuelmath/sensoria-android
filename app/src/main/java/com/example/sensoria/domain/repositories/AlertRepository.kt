package com.example.sensoria.domain.repositories

import com.example.sensoria.domain.models.Alert

interface AlertRepository {
    suspend fun getAlerts(companyId: String): Result<List<Alert>>
    suspend fun resolveAlert(alertId: Int): Result<Unit>
}