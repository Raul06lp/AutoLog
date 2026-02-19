package com.carlafdez.autolog.data.remote.api

import com.carlafdez.autolog.data.remote.dto.ClienteDTO
import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.data.remote.dto.MecanicoDTO
import com.carlafdez.autolog.data.remote.dto.RegisterRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    // Endpoints de Mecánico
    @POST("api/mecanicos/login")
    suspend fun loginMecanico(@Body request: LoginRequestDTO): MecanicoDTO

    @POST("api/mecanicos/registro")
    suspend fun registerMecanico(@Body request: RegisterRequestDTO): MecanicoDTO

    // Endpoints de Cliente
    @POST("api/clientes/login")
    suspend fun loginCliente(@Body request: LoginRequestDTO): ClienteDTO

    @POST("api/clientes/registro")
    suspend fun registerCliente(@Body request: RegisterRequestDTO): ClienteDTO
}
