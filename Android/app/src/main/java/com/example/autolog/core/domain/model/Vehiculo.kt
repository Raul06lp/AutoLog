package com.example.autolog.core.data.remote.model

import java.time.LocalDateTime

data class Vehiculo(
    val idVehiculo: Long,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val color: String?,
    val kilometraje: Int?,
    val observaciones: String?,
    val medidasTomadas: String?,
    val estadoRevision: String?,
    val imagenBase64: String?,
    val idCliente: Long,
    val nombreCliente: String?,
    val emailCliente: String?,
    val idMecanico: Long?,
    val nombreMecanico: String?,
    val emailMecanico: String?,
    val fechaIngreso: LocalDateTime?
)
