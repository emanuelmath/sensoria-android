package com.example.sensoria.domain.models

data class TelemetryReading(
    val value: Float,
    val unit: String,
    val timestamp: String?
)