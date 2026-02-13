package com.example.autolog.core.data.remote.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.autolog.core.data.remote.dto.vehiculo.VehiculoDTO
import com.example.autolog.core.data.remote.dto.vehiculo.VehiculoRequestDTO
import com.example.autolog.core.data.remote.model.Vehiculo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object VehiculoMapper {
    
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    /**
     * Convierte VehiculoDTO a modelo de dominio Vehiculo
     */
    fun toDomain(dto: VehiculoDTO): Vehiculo {
        return Vehiculo(
            idVehiculo = dto.idVehiculo,
            matricula = dto.matricula,
            marca = dto.marca,
            modelo = dto.modelo,
            anio = dto.anio,
            color = dto.color,
            kilometraje = dto.kilometraje,
            observaciones = dto.observaciones,
            medidasTomadas = dto.medidasTomadas,
            estadoRevision = dto.estadoRevision,
            imagenBase64 = dto.imagenBase64,
            idCliente = dto.idCliente,
            nombreCliente = dto.nombreCliente,
            emailCliente = dto.emailCliente,
            idMecanico = dto.idMecanico,
            nombreMecanico = dto.nombreMecanico,
            emailMecanico = dto.emailMecanico,
            fechaIngreso = dto.fechaIngreso?.let { parseDateTime(it) }
        )
    }
    
    /**
     * Convierte lista de VehiculoDTO a lista de modelos Vehiculo
     */
    fun toDomainList(dtoList: List<VehiculoDTO>): List<Vehiculo> {
        return dtoList.map { toDomain(it) }
    }
    
    /**
     * Crea un VehiculoRequestDTO a partir de datos del vehículo
     */
    fun toRequestDTO(
        matricula: String,
        marca: String,
        modelo: String,
        anio: Int,
        color: String?,
        kilometraje: Int?,
        observaciones: String?,
        medidasTomadas: String?,
        estadoRevision: String?,
        imagenBase64: String?,
        idCliente: Long,
        idMecanico: Long?
    ): VehiculoRequestDTO {
        return VehiculoRequestDTO(
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            color = color,
            kilometraje = kilometraje,
            observaciones = observaciones,
            medidasTomadas = medidasTomadas,
            estadoRevision = estadoRevision,
            imagenBase64 = imagenBase64,
            idCliente = idCliente,
            idMecanico = idMecanico
        )
    }
    
    /**
     * Convierte un modelo Vehiculo a VehiculoRequestDTO
     */
    fun toRequestDTO(vehiculo: Vehiculo): VehiculoRequestDTO {
        return VehiculoRequestDTO(
            matricula = vehiculo.matricula,
            marca = vehiculo.marca,
            modelo = vehiculo.modelo,
            anio = vehiculo.anio,
            color = vehiculo.color,
            kilometraje = vehiculo.kilometraje,
            observaciones = vehiculo.observaciones,
            medidasTomadas = vehiculo.medidasTomadas,
            estadoRevision = vehiculo.estadoRevision,
            imagenBase64 = vehiculo.imagenBase64,
            idCliente = vehiculo.idCliente,
            idMecanico = vehiculo.idMecanico
        )
    }
    
    /**
     * Parsea una cadena de fecha a LocalDateTime
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDateTime(dateString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateString, dateFormatter)
        } catch (e: Exception) {
            null
        }
    }
}
