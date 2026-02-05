package com.example.autolog.feature.vehicle.domain.repository

import com.example.autolog.feature.vehicle.domain.model.Vehicle

class FakeVehicleRepository: VehiculoRepository {
    override suspend fun ibsertVehiculo(vehicle: Vehicle) {

    }

    override suspend fun getVehiculoById(id: String): Vehicle? {

        return null
    }

    override suspend fun updateVehicle(vehicle: Vehicle) {

    }
}