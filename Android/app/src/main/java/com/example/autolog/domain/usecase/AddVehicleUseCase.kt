package com.example.autolog.domain.usecase

import com.example.autolog.domain.model.Vehicle
import com.example.autolog.domain.repository.VehiculoRepository

class AddVehicleUseCase(val repository: VehiculoRepository) {

    suspend operator fun invoke(vehicle: Vehicle) {
        if(vehicle.matricula.isBlank()) throw IllegalArgumentException("ghhghf")

        repository.insertVehiculo(vehicle = vehicle)
    }

}
