package com.example.autolog.core.data.remote.dto.mecanico

import com.google.gson.annotations.SerializedName

data class MecanicoDTO(
    @SerializedName("idMecanico")
    val idMecanico: Long,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("cantidadVehiculos")
    val cantidadVehiculos: Int
)
