package com.example.sensoria.domain.models

data class TicketDetail(
    val ticket: Ticket,
    val messages: List<TicketMessage>
)