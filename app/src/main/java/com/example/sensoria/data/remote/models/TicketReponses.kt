package com.example.sensoria.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketListResponse(
    val data: List<TicketDto>
)

@Serializable
data class TicketDto(
    val id: Int,
    @SerialName("ticket_number") val ticketNumber: String,
    val subject: String,
    val category: String,
    val priority: String,
    val status: String,
    @SerialName("created_at") val createdAt: String
)