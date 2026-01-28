package com.example.autolog.data

data class Usuario(
    val name: String,
    val email: String,
    val esMecanico: Boolean,
    val vehiculos: List<Vehiculo>?
)