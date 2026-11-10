package com.example.sensoria.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AlertListResponse(val data: List<AlertDto>)

@Serializable
data class AlertDto(
    val id: Int,
    val severity: String,
    val title: String,
    val message: String,
    val is_resolved: Boolean,
    val created_at: String,
    val device_name: String
)