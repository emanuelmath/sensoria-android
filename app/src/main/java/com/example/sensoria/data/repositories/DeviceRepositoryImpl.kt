package com.example.sensoria.data.repositories

import com.example.sensoria.data.remote.DeviceApi
import com.example.sensoria.domain.models.Device
import com.example.sensoria.domain.models.DeviceTelemetry
import com.example.sensoria.domain.models.TelemetryReading
import com.example.sensoria.domain.repositories.DeviceRepository
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class DeviceRepositoryImpl(private val api: DeviceApi): DeviceRepository {
    override suspend fun getDevices(): Result<List<Device>> {
        return try {
            val response = api.getDevices()

            val devicesDomain = response.data.map { dto ->
                Device(
                    id = dto.id,
                    name = dto.name,
                    type = dto.type,
                    lastReading = dto.lastReading,
                    isOnline = dto.isOnline
                )
            }
            Result.success(devicesDomain)

        } catch(e: Exception) {
            val friendlyMessage = when (e) {
                is HttpException -> {
                    if (e.code() == 401) "Sesión expirada o credenciales incorrectas."
                    else "Ocurrió un problema en el servidor. Intente más tarde."
                }
                is IOException -> "No hay conexión al servidor. Revisa tu red."
                is SerializationException -> "Error de procesamiento interno de datos."
                else -> "Ocurrió un error inesperado. Contacte a soporte."
            }
            Result.failure(Exception(friendlyMessage))
        }
    }

    override suspend fun getDeviceTelemetry(deviceId: String): Result<DeviceTelemetry> {
        return try {
            val response = api.getDeviceTelemetry(deviceId)

            val domainReadings = response.readings.map { dto ->
                TelemetryReading(
                    value = dto.value,
                    unit = dto.unit,
                    timestamp = dto.timestamp
                )
            }

            val deviceTelemetry = DeviceTelemetry(
                deviceName = response.deviceName,
                readings = domainReadings
            )

            Result.success(deviceTelemetry)

        } catch (e: Exception) {
            val friendlyMessage = when (e) {
                is HttpException -> if (e.code() == 404) "Dispositivo no encontrado" else "Error del servidor"
                is IOException -> "Sin conexión a internet"
                else -> "Error inesperado"
            }
            Result.failure(Exception(friendlyMessage))
        }
    }
}