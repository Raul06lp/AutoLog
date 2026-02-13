package com.example.autolog.domain.model

import android.net.Uri

data class Vehicle(
    val id: String,
    val imagen: Uri? = null,
    val cliente: User,
    val marca: String,
    val modelo: String,
    val matricula: String,
    val year: Int,
    val kilometros: String,
    val color: String,
    val observaciones: String
)