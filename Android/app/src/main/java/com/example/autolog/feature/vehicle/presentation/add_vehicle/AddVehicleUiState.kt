package com.example.autolog.feature.vehicle.presentation.add_vehicle

data class AddVehicleUiState(
    var imageUri : String ="",
    var clientName : String = "",
    var marca: String = "",
    var modelo: String = "",
    var matricula: String = "",
    var year: String = "",
    var kilometros: String = "",
    var color: String = "",
    var observaciones: String = ""
)
