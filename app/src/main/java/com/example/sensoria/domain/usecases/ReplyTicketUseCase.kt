package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.TicketMessage
import com.example.sensoria.domain.repositories.TicketRepository
import javax.inject.Inject

class ReplyTicketUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(id: Int, message: String): Result<TicketMessage> =
        repository.replyTicket(id, message)
}