package com.example.autolog.feature.vehicle.domain.model

import com.example.autolog.core.domain.model.User

data class Vehicle(
    val id: String,
    val imageUrl: String,
    val cliente: User,
    val marca: String,
    val modelo: String,
    val matricula: String,
    val year: Int,
    val kilometros: String,
    val color: String,
    val observaciones: String,
    val medidas: String = ""
)