package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.Ticket
import com.example.sensoria.domain.repositories.TicketRepository
import javax.inject.Inject

class GetTicketsUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(): Result<List<Ticket>> {
        return repository.getTickets()
    }
}