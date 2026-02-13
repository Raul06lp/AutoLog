package com.example.autolog.core.data.remote.dto.cliente

import com.google.gson.annotations.SerializedName

data class LoginDTO(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("contrasena")
    val contrasena: String
)
