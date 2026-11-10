package com.example.sensoria.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateTime(dateString: String?): String {
    if (dateString.isNullOrBlank()) return "Fecha no disponible"

    return try {
        val instant = Instant.parse(dateString)

        val formatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm", Locale("es", "SV"))
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)

    } catch (e: Exception) {
        "Fecha no disponible"
    }
}