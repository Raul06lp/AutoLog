package com.example.autolog.core.data.remote.dto.cliente

import com.google.gson.annotations.SerializedName

data class ClienteDTO(
    @SerializedName("idCliente")
    val idCliente: Long,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("cantidadVehiculos")
    val cantidadVehiculos: Int
)
