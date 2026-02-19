package com.carlafdez.autolog.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDTO(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("contrasena") val contrasena: String
)
