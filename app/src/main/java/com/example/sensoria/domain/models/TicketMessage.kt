package com.example.sensoria.domain.models

data class TicketMessage(
    val id: Int,
    val message: String,
    val createdAt: String,
    val userName: String,
    val userRole: String,
    val isMine: Boolean
)