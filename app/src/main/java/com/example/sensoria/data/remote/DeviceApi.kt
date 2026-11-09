package com.example.sensoria.data.remote

import com.example.sensoria.data.remote.models.DeviceListResponse
import com.example.sensoria.data.remote.models.TelemetryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DeviceApi {
    @GET("v1/devices")
    suspend fun getDevices(): DeviceListResponse
    @GET("v1/devices/{id}/telemetry")
    suspend fun getDeviceTelemetry(
        @Path("id") deviceId: String
    ): TelemetryResponse
}