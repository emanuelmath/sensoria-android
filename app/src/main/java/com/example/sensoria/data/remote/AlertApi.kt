package com.example.sensoria.data.remote

import com.example.sensoria.data.remote.models.AlertListResponse
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AlertApi {
    @GET("v1/alerts")
    suspend fun getAlerts(@Query("company_id") companyId: String): AlertListResponse

    @PUT("v1/alerts/{id}/resolve")
    suspend fun resolveAlert(@Path("id") alertId: Int)
}