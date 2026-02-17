package com.carlafdez.autolog.domain.model

data class Vehiculo (
    val id: Long,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val color: String,
    val kilometraje: Int,
    val observaciones: String,
    val estadoRevision: String,
    val imagenBase64: String?,
    val idCliente: Long,
    val nombreCliente: String,
    val idMecanico: Long?,
    val nombreMecanico: String?,
    val fechaIngreso: String
)