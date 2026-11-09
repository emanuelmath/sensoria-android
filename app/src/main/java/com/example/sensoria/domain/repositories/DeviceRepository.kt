package com.example.sensoria.domain.repositories

import com.example.sensoria.domain.models.Device
import com.example.sensoria.domain.models.DeviceTelemetry

interface DeviceRepository {
    suspend fun getDevices(): Result<List<Device>>
    suspend fun getDeviceTelemetry(deviceId: String): Result<DeviceTelemetry>
}