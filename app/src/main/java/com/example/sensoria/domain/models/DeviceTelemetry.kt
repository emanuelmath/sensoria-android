package com.example.sensoria.domain.models

data class DeviceTelemetry(
    val deviceName: String,
    val readings: List<TelemetryReading>
)