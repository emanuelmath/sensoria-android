package com.example.sensoria.domain.repositories

import com.example.sensoria.domain.models.Ticket
import com.example.sensoria.domain.models.TicketDetail
import com.example.sensoria.domain.models.TicketMessage

interface TicketRepository {
    suspend fun getTickets(): Result<List<Ticket>>
    suspend fun getTicketDetail(id: Int): Result<TicketDetail>
    suspend fun replyTicket(id: Int, message: String): Result<TicketMessage>
    suspend fun createTicket(subject: String, category: String, priority: String, description: String): Result<Int>
}