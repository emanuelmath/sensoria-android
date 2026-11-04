package com.example.sensoria.data.remote

import com.example.sensoria.data.local.AuthPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authPreferences: AuthPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { authPreferences.getAuthToken.first() }
        val companyId = runBlocking { authPreferences.getCurrentCompanyId.first() }

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        if (!companyId.isNullOrEmpty()) {
            requestBuilder.addHeader("X-Company-Id", companyId)
        }

        requestBuilder.addHeader("Accept", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}