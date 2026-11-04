package com.example.sensoria.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
data class DashboardRoute(val companyId: String)