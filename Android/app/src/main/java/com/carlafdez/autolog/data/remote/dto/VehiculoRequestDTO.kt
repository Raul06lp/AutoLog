package com.carlafdez.autolog.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VehiculoRequestDTO(
    @SerializedName("matricula") val matricula: String,
    @SerializedName("marca") val marca: String,
    @SerializedName("modelo") val modelo: String,
    @SerializedName("anio") val anio: Int,
    @SerializedName("color") val color: String?,
    @SerializedName("kilometraje") val kilometraje: Int?,
    @SerializedName("observaciones") val observaciones: String?,
    @SerializedName("medidasTomadas") val medidasTomadas: String?,
    @SerializedName("idCliente") val idCliente: Long?,
    @SerializedName("idMecanico") val idMecanico: Long?
)
