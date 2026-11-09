package com.example.sensoria.domain.models

data class Ticket(
    val id: Int,
    val ticketNumber: String,
    val subject: String,
    val category: String,
    val priority: String,
    val status: String,
    val createdAt: String
)