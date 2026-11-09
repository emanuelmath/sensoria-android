package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.DeviceTelemetry
import com.example.sensoria.domain.repositories.DeviceRepository
import javax.inject.Inject

class GetDeviceTelemetryUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String): Result<DeviceTelemetry> {
        return repository.getDeviceTelemetry(deviceId)
    }
}