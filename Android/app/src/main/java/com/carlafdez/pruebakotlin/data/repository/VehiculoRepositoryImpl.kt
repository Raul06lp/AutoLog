package com.carlafdez.pruebakotlin.data.repository

import com.carlafdez.pruebakotlin.data.remote.api.VehiculoApi
import com.carlafdez.pruebakotlin.data.remote.mapper.toVehicle
import com.carlafdez.pruebakotlin.domain.model.Vehiculo
import com.carlafdez.pruebakotlin.domain.repository.VehiculoRepository

class VehiculoRepositoryImpl(private val api: VehiculoApi): VehiculoRepository {
    override suspend fun getVehiclesByMecanico(idMecanico: Long): List<Vehiculo> {
        return api.getVehiculosByMecanico(idMecanico).map { it.toVehicle() }
    }
}