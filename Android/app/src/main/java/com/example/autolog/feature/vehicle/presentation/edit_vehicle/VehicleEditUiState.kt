package com.example.autolog.feature.vehicle.presentation.edit_vehicle


import android.net.Uri
import com.example.autolog.feature.vehicle.domain.model.Vehicle

data class VehicleEditUiState(
    val originalVehicle: Vehicle? = null,
    val imageUri: Uri? = null,
    val clientName: String = "",
    val marca: String = "",
    val modelo: String = "",
    val matricula: String = "",
    val year: String = "",
    val kilometros: String = "",
    val color: String = "",
    val observaciones: String = "",
    val medidas: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)