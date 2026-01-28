package com.example.autolog.data

data class Vehiculo(
    val id: String,
    val imageUrl: String,
    val cliente: Usuario,
    val marca: String,
    val modelo: String,
    val matricula: String,
    val year: Int,
    val kilometros: String,
    val color: String,
    val observaciones: String,
    val medidas: String = ""
)