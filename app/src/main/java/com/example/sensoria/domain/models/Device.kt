package com.example.sensoria.domain.models

data class Device(
    val id: String,
    val name: String,
    val type: String,
    val isOnline: Boolean,
    val lastReading: String
)