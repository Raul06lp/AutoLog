package com.carlafdez.autolog.domain.repository

import android.net.Uri
import com.carlafdez.autolog.domain.model.Cliente
import com.carlafdez.autolog.domain.model.Vehiculo

interface VehiculoRepository {
    suspend fun getVehiclesByMecanico(idMecanico: Long): List<Vehiculo>
    suspend fun getVehiculoById(id: Long): Vehiculo?
    suspend fun crearVehiculo(
        matricula: String, marca: String, modelo: String, anio: Int,
        color: String?, kilometraje: Int?, observaciones: String?,
        idCliente: Long, idMecanico: Long, imagenUri: Uri?
    ): Vehiculo
    suspend fun actualizarVehiculo(
        id: Long, matricula: String, marca: String, modelo: String, anio: Int,
        color: String?, kilometraje: Int?, observaciones: String?,
        medidasTomadas: String?, idCliente: Long, idMecanico: Long?,
        nuevaImagenUri: Uri?
    ): Vehiculo
    suspend fun getClientes(): List<Cliente>
}
