package com.example.autolog.feature.vehicle.presentation.detail_vehicle

sealed interface VehicleDetailAction {
    data object NavigateBack : VehicleDetailAction
}
