package com.example.autolog.core.data.remote.dto.mecanico

import com.google.gson.annotations.SerializedName

data class MecanicoRequestDTO(
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("contrasena")
    val contrasena: String
)
