package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.repositories.TicketRepository
import javax.inject.Inject

class CreateTicketUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(subject: String, category: String, priority: String, description: String): Result<Int> =
        repository.createTicket(subject, category, priority, description)
}