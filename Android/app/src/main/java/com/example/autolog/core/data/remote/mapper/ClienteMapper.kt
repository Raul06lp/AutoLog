package com.example.autolog.core.data.remote.mapper

import com.example.autolog.core.data.remote.dto.cliente.ClienteDTO
import com.example.autolog.core.data.remote.dto.cliente.ClienteRequestDTO
import com.example.autolog.core.data.remote.model.Cliente

object ClienteMapper {
    
    /**
     * Convierte ClienteDTO a modelo de dominio Cliente
     */
    fun toDomain(dto: ClienteDTO): Cliente {
        return Cliente(
            idCliente = dto.idCliente,
            nombre = dto.nombre,
            email = dto.email,
            cantidadVehiculos = dto.cantidadVehiculos
        )
    }
    
    /**
     * Convierte lista de ClienteDTO a lista de modelos Cliente
     */
    fun toDomainList(dtoList: List<ClienteDTO>): List<Cliente> {
        return dtoList.map { toDomain(it) }
    }
    
    /**
     * Crea un ClienteRequestDTO a partir de datos de registro
     */
    fun toRequestDTO(
        nombre: String,
        email: String,
        contrasena: String
    ): ClienteRequestDTO {
        return ClienteRequestDTO(
            nombre = nombre,
            email = email,
            contrasena = contrasena
        )
    }
}
