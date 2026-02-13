package com.example.autolog.presentation.screens.add_vehicle

import android.net.Uri

data class AddVehicleUiState(
    var imageUri : Uri? = null,
    var clientName : String = "",
    var marca: String = "",
    var modelo: String = "",
    var matricula: String = "",
    var year: String = "",
    var kilometros: String = "",
    var color: String = "",
    var observaciones: String = ""
)
