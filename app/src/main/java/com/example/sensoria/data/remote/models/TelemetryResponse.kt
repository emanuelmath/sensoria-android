package com.example.sensoria.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelemetryResponse(
    @SerialName("device_id") val deviceId: Int,
    @SerialName("device_name") val deviceName: String,
    val readings: List<TelemetryReadingDto>
)

@Serializable
data class TelemetryReadingDto(
    val id: Int,
    val value: Float,
    val unit: String,
    val timestamp: String?
)