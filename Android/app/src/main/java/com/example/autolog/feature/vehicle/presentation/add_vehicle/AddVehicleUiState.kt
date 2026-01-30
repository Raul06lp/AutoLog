package com.example.autolog.feature.vehicle.presentation.add_vehicle

data class AddVehicleUiState(
    val imageUri : String ="",
    val clientName : String = "",
    val marca: String = "",
    val modelo: String = "",
    val matricula: String = "",
    val year: String = "",
    val kilometros: String = "",
    val color: String = "",
    val observaciones: String = ""
)
