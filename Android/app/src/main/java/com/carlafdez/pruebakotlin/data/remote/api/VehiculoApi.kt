package com.carlafdez.pruebakotlin.data.remote.api

import com.carlafdez.pruebakotlin.data.remote.dto.VehiculoDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface VehiculoApi {

    @GET("api/vehiculos/mecanico/{idMecanico}")
    suspend fun getVehiculosByMecanico(@Path("idMecanico") idMecanico: Long): List<VehiculoDTO>

    @GET("api/vehiculos/{id}")
    suspend fun getVehiculoById(@Path("id") id: Long): VehiculoDTO}