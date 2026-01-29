package com.example.autolog.feature.vehicle.presentation.add_car

import com.example.autolog.feature.vehicle.domain.model.Vehicle

sealed interface AddCarAction {
    data object NavigateBack : AddCarAction
    data class AddCar(val vehicle: Vehicle): AddCarAction

}