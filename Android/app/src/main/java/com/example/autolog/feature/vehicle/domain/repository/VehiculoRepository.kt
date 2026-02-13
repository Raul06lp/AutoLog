package com.example.autolog.feature.vehicle.domain.repository

import com.example.autolog.feature.vehicle.domain.model.Vehicle

interface VehiculoRepository {

    suspend fun insertVehiculo(vehicle: Vehicle)
    suspend fun getVehiculoById(id: String): Vehicle?
    suspend fun updateVehicle(vehicle: Vehicle)


}