package com.carlafdez.pruebakotlin.domain.repository

import com.carlafdez.pruebakotlin.domain.model.Vehiculo

interface VehiculoRepository {
    suspend fun getVehiclesByMecanico(idMecanico: Long): List<Vehiculo>

}