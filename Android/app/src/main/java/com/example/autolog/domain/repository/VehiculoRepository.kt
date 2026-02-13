package com.example.autolog.domain.repository

import com.example.autolog.domain.model.Vehicle

interface VehiculoRepository {

    suspend fun insertVehiculo(vehicle: Vehicle)
    suspend fun getVehiculoById(id: String): Vehicle?
    suspend fun updateVehicle(vehicle: Vehicle)


}