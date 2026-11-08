package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.Device
import com.example.sensoria.domain.repositories.DeviceRepository 
import javax.inject.Inject

class GetDevicesUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(): Result<List<Device>> {
        return repository.getDevices()
    }
}