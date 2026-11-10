package com.example.sensoria.data.remote

import com.example.sensoria.data.local.AuthPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.example.sensoria.BuildConfig

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    private fun createRetrofit(authPreferences: AuthPreferences): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(authPreferences))
            .retryOnConnectionFailure(true)
            .build()

        val networkJson = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
    }

    // 2. Creación de APIs ultra limpia
    fun createAuthApi(authPreferences: AuthPreferences): AuthApi =
        createRetrofit(authPreferences).create(AuthApi::class.java)

    fun createDeviceApi(authPreferences: AuthPreferences): DeviceApi =
        createRetrofit(authPreferences).create(DeviceApi::class.java)

    fun createTicketApi(authPreferences: AuthPreferences): TicketApi =
        createRetrofit(authPreferences).create(TicketApi::class.java)
}