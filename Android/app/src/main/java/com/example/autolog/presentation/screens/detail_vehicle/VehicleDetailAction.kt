package com.example.autolog.presentation.screens.detail_vehicle

sealed interface VehicleDetailAction {
    data object NavigateBack : VehicleDetailAction
}
