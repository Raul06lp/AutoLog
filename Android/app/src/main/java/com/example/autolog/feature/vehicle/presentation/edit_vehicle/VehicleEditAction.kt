package com.example.autolog.feature.vehicle.presentation.edit_vehicle

import android.net.Uri

sealed interface VehicleEditAction {
    data object NavigateBack : VehicleEditAction
    data object SaveVehicle : VehicleEditAction

    // Acciones para actualizar cada campo del formulario
    data class UpdateImageUri(val uri: Uri?) : VehicleEditAction
    data class UpdateClientName(val name: String) : VehicleEditAction
    data class UpdateMarca(val marca: String) : VehicleEditAction
    data class UpdateModelo(val modelo: String) : VehicleEditAction
    data class UpdateMatricula(val matricula: String) : VehicleEditAction
    data class UpdateYear(val year: String) : VehicleEditAction
    data class UpdateKilometros(val kilometros: String) : VehicleEditAction
    data class UpdateColor(val color: String) : VehicleEditAction
    data class UpdateObservaciones(val observaciones: String) : VehicleEditAction
    data class UpdateMedidas(val medidas: String) : VehicleEditAction
}