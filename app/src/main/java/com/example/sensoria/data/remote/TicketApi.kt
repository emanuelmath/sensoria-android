package com.example.sensoria.data.remote

import com.example.sensoria.data.remote.models.CreateTicketRequest
import com.example.sensoria.data.remote.models.CreateTicketResponse
import com.example.sensoria.data.remote.models.ReplyRequest
import com.example.sensoria.data.remote.models.ReplyResponse
import com.example.sensoria.data.remote.models.TicketDetailResponse
import com.example.sensoria.data.remote.models.TicketListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketApi {
    @GET("v1/tickets")
    suspend fun getTickets(): TicketListResponse

    @GET("v1/tickets/{id}")
    suspend fun getTicketDetail(@Path("id") id: Int): TicketDetailResponse

    @POST("v1/tickets/{id}/reply")
    suspend fun replyTicket(
        @Path("id") id: Int,
        @Body request: ReplyRequest
    ): ReplyResponse

    @POST("v1/tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): CreateTicketResponse
}