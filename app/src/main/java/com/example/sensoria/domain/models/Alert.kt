package com.example.sensoria.domain.models

data class Alert(
    val id: Int,
    val severity: String,
    val title: String,
    val message: String,
    val isResolved: Boolean,
    val date: String,
    val source: String
)