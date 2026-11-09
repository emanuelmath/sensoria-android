package com.example.sensoria.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val id: String,
    val name: String,
    val role: String
)