package com.example.autolog.feature.vehicle.domain.usecase

import com.example.autolog.feature.vehicle.domain.model.Vehicle
import com.example.autolog.feature.vehicle.domain.repository.VehiculoRepository

class AddVehicleUseCase(val repository: VehiculoRepository) {

    suspend operator fun invoke(vehicle: Vehicle) {
        if(vehicle.matricula.isBlank()) throw IllegalArgumentException("ghhghf")

        repository.insertVehiculo(vehicle = vehicle)
    }

}
