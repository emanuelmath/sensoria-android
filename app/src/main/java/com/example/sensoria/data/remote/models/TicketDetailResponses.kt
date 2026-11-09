package com.example.sensoria.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketDetailResponse(
    val data: TicketDetailDto
)

@Serializable
data class TicketDetailDto(
    val id: Int,
    @SerialName("ticket_number") val ticketNumber: String,
    val subject: String,
    val category: String,
    val priority: String,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    val messages: List<TicketMessageDto> = emptyList()
)

@Serializable
data class TicketMessageDto(
    val id: Int,
    @SerialName("user_id") val userId: Int,
    val message: String,
    @SerialName("created_at") val createdAt: String,
    val user: TicketUserDto
)

@Serializable
data class TicketUserDto(
    val id: Int,
    val name: String,
    @SerialName("global_role") val globalRole: String
)

@Serializable
data class ReplyRequest(
    val message: String
)

@Serializable
data class ReplyResponse(
    val data: TicketMessageDto
)

@Serializable
data class CreateTicketRequest(
    val subject: String,
    val category: String,
    val priority: String,
    val description: String
)

@Serializable
data class CreateTicketResponse(
    val message: String,
    @SerialName("ticket_id") val ticketId: Int
)