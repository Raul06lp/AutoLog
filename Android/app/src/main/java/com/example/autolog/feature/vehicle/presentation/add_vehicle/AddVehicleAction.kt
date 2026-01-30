package com.example.autolog.feature.vehicle.presentation.add_vehicle

import com.example.autolog.feature.vehicle.domain.model.Vehicle

sealed interface AddVehicleAction {
    data object NavigateBack : AddVehicleAction
    data class AddCar(val vehicle: Vehicle): AddVehicleAction

}