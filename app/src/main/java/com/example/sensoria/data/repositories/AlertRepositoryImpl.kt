package com.example.sensoria.data.repositories

import com.example.sensoria.data.remote.AlertApi
import com.example.sensoria.domain.repositories.AlertRepository
import com.example.sensoria.domain.models.Alert

class AlertRepositoryImpl(private val api: AlertApi) : AlertRepository {
    override suspend fun getAlerts(companyId: String): Result<List<Alert>> {
        return try {
            val response = api.getAlerts(companyId)
            val domainAlerts = response.data.map { dto ->
                Alert(
                    id = dto.id,
                    severity = dto.severity,
                    title = dto.title,
                    message = dto.message,
                    isResolved = dto.is_resolved,
                    date = dto.created_at,
                    source = dto.device_name
                )
            }
            Result.success(domainAlerts)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("Error al cargar alertas"))
        }
    }

    override suspend fun resolveAlert(alertId: Int): Result<Unit> {
        return try {
            api.resolveAlert(alertId)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("No se pudo resolver la alerta"))
        }
    }
}