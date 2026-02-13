package com.example.autolog.core.data.remote.dto.vehiculo

import com.google.gson.annotations.SerializedName

data class VehiculoDTO(
    @SerializedName("idVehiculo")
    val idVehiculo: Long,
    
    @SerializedName("matricula")
    val matricula: String,
    
    @SerializedName("marca")
    val marca: String,
    
    @SerializedName("modelo")
    val modelo: String,
    
    @SerializedName("anio")
    val anio: Int,
    
    @SerializedName("color")
    val color: String?,
    
    @SerializedName("kilometraje")
    val kilometraje: Int?,
    
    @SerializedName("observaciones")
    val observaciones: String?,
    
    @SerializedName("medidasTomadas")
    val medidasTomadas: String?,
    
    @SerializedName("estadoRevision")
    val estadoRevision: String?,
    
    @SerializedName("imagenBase64")
    val imagenBase64: String?,
    
    @SerializedName("idCliente")
    val idCliente: Long,
    
    @SerializedName("nombreCliente")
    val nombreCliente: String?,
    
    @SerializedName("emailCliente")
    val emailCliente: String?,
    
    @SerializedName("idMecanico")
    val idMecanico: Long?,
    
    @SerializedName("nombreMecanico")
    val nombreMecanico: String?,
    
    @SerializedName("emailMecanico")
    val emailMecanico: String?,
    
    @SerializedName("fechaIngreso")
    val fechaIngreso: String?
)
