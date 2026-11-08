package com.example.sensoria.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceListResponse(
    val data: List<DeviceDto>
)

@Serializable
data class DeviceDto(
    val id: String,
    val name: String,
    val type: String,
    val lastReading: String,
    val isOnline: Boolean
)