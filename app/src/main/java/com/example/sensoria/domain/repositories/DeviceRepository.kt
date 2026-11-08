package com.example.sensoria.domain.repositories

import com.example.sensoria.domain.models.Device

interface DeviceRepository {
    suspend fun getDevices(): Result<List<Device>>
}