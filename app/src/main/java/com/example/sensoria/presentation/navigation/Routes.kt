package com.example.sensoria.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable data object LoginRoute
@Serializable data class MainRoute(val companyId: String)
@Serializable data class DeviceDetailRoute(val deviceId: String)

@Serializable
data class CompanySelectionRoute(val companiesJson: String)