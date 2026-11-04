package com.example.sensoria.domain.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val companies: List<Company>,
    val currentCompanyId: String?
)