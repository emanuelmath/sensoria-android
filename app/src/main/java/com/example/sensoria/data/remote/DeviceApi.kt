package com.example.sensoria.data.remote

import com.example.sensoria.data.remote.models.DeviceListResponse
import retrofit2.http.GET

interface DeviceApi {
    @GET("v1/devices")
    suspend fun getDevices(): DeviceListResponse
}