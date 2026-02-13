package com.example.autolog.core.data.remote.mapper

import com.example.autolog.core.data.remote.dto.mecanico.MecanicoDTO
import com.example.autolog.core.data.remote.dto.mecanico.MecanicoRequestDTO
import com.example.autolog.core.data.remote.model.Mecanico

object MecanicoMapper {
    
    /**
     * Convierte MecanicoDTO a modelo de dominio Mecanico
     */
    fun toDomain(dto: MecanicoDTO): Mecanico {
        return Mecanico(
            idMecanico = dto.idMecanico,
            nombre = dto.nombre,
            email = dto.email,
            cantidadVehiculos = dto.cantidadVehiculos
        )
    }
    
    /**
     * Convierte lista de MecanicoDTO a lista de modelos Mecanico
     */
    fun toDomainList(dtoList: List<MecanicoDTO>): List<Mecanico> {
        return dtoList.map { toDomain(it) }
    }
    
    /**
     * Crea un MecanicoRequestDTO a partir de datos de registro
     */
    fun toRequestDTO(
        nombre: String,
        email: String,
        contrasena: String
    ): MecanicoRequestDTO {
        return MecanicoRequestDTO(
            nombre = nombre,
            email = email,
            contrasena = contrasena
        )
    }
}
