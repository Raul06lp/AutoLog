package com.example.autolog.core.domain.model

import com.example.autolog.feature.vehicle.domain.model.Vehicle

data class User(
    val name: String,
    val email: String,
    val esMecanico: Boolean,
    val vehicles: List<Vehicle>?
)