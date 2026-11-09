package com.example.sensoria.domain.usecases

import com.example.sensoria.domain.models.TicketDetail
import com.example.sensoria.domain.repositories.TicketRepository
import javax.inject.Inject

class GetTicketDetailUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(id: Int): Result<TicketDetail> = repository.getTicketDetail(id)
}