package com.example.sensoria.data.repositories

import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.data.remote.TicketApi
import com.example.sensoria.data.remote.models.CreateTicketRequest
import com.example.sensoria.data.remote.models.ReplyRequest
import com.example.sensoria.domain.models.Ticket
import com.example.sensoria.domain.models.TicketMessage
import com.example.sensoria.domain.models.TicketDetail
import com.example.sensoria.domain.repositories.TicketRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class TicketRepositoryImpl(
    private val api: TicketApi,
    private val authPreferences: AuthPreferences
) : TicketRepository {

    override suspend fun getTickets(): Result<List<Ticket>> {
        return try {
            val response = api.getTickets()
            val domainTickets = response.data.map { dto ->
                Ticket(
                    id = dto.id,
                    ticketNumber = dto.ticketNumber,
                    subject = dto.subject,
                    category = dto.category,
                    priority = dto.priority,
                    status = dto.status,
                    createdAt = dto.createdAt
                )
            }
            Result.success(domainTickets)
        } catch (e: Exception) {
            val message = when (e) {
                is HttpException -> if (e.code() == 401) "Sesión expirada." else "Error del servidor."
                is IOException -> "Sin conexión a internet."
                is SerializationException -> "Error al procesar datos del servidor."
                else -> "Ocurrió un error inesperado."
            }
            Result.failure(Exception(message))
        }
    }

    override suspend fun getTicketDetail(id: Int): Result<TicketDetail> {
        return try {
            val response = api.getTicketDetail(id)
            val currentUserName = authPreferences.getUserName.first()

            val domainMessages = response.data.messages.map { dto ->
                TicketMessage(
                    id = dto.id,
                    message = dto.message,
                    createdAt = dto.createdAt,
                    userName = dto.user.name,
                    userRole = dto.user.globalRole,
                    isMine = dto.user.name == currentUserName
                )
            }

            val ticket = Ticket(
                id = response.data.id,
                ticketNumber = response.data.ticketNumber,
                subject = response.data.subject,
                category = response.data.category,
                priority = response.data.priority,
                status = response.data.status,
                createdAt = response.data.createdAt
            )

            Result.success(TicketDetail(ticket, domainMessages))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception(handleError(e)))
        }
    }

    override suspend fun replyTicket(id: Int, message: String): Result<TicketMessage> {
        return try {
            val response = api.replyTicket(id, ReplyRequest(message))
            val currentUserName = authPreferences.getUserName.first()

            val domainMessage = TicketMessage(
                id = response.data.id,
                message = response.data.message,
                createdAt = response.data.createdAt,
                userName = response.data.user.name,
                userRole = response.data.user.globalRole,
                isMine = response.data.user.name == currentUserName
            )
            Result.success(domainMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception(handleError(e)))
        }
    }

    override suspend fun createTicket(subject: String, category: String, priority: String, description: String): Result<Int> {
        return try {
            val request = CreateTicketRequest(subject, category, priority, description)
            val response = api.createTicket(request)
            Result.success(response.ticketId)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception(handleError(e)))
        }
    }

    private fun handleError(e: Exception): String {
        return when (e) {
            is HttpException -> if (e.code() == 404) "Ticket no encontrado." else "Error del servidor."
            is IOException -> "Sin conexión a internet."
            is SerializationException -> "Error procesando datos."
            else -> "Ocurrió un error inesperado."
        }
    }
}