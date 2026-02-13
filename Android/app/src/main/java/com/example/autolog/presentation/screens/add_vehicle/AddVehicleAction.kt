package com.example.autolog.presentation.screens.add_vehicle

import com.example.autolog.domain.model.Vehicle

sealed interface AddVehicleAction {
    data object NavigateBack : AddVehicleAction
    data class AddCar(val vehicle: Vehicle): AddVehicleAction

}