package com.carlafdez.autolog.data.remote.mapper

import com.carlafdez.autolog.data.remote.dto.VehiculoDTO
import com.carlafdez.autolog.domain.model.Vehiculo

fun VehiculoDTO.toVehicle() = Vehiculo(
    id = idVehiculo,
    matricula = matricula,
    marca = marca,
    modelo = modelo,
    anio = anio,
    color = color ?: "",
    kilometraje = kilometraje ?: 0,
    observaciones = observaciones ?: "",
    estadoRevision = estadoRevision ?: "pendiente",
    imagenBase64 = imagenBase64,
    idCliente = idCliente,
    nombreCliente = nombreCliente ?: "",
    idMecanico = idMecanico,
    nombreMecanico = nombreMecanico,
    fechaIngreso = fechaIngreso ?: ""
)