package com.carlafdez.autolog.data.remote.api

import com.carlafdez.autolog.data.remote.dto.ClienteDTO
import com.carlafdez.autolog.data.remote.dto.ClienteRequestDTO
import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.data.remote.dto.MecanicoDTO
import com.carlafdez.autolog.data.remote.dto.MecanicoRequestDTO
import com.carlafdez.autolog.data.remote.dto.RegisterRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {

    @POST("api/mecanicos/login")
    suspend fun loginMecanico(@Body request: LoginRequestDTO): MecanicoDTO

    @POST("api/mecanicos/registro")
    suspend fun registerMecanico(@Body request: RegisterRequestDTO): MecanicoDTO

    @PUT("api/mecanicos/{id}")
    suspend fun updateMecanico(@Path("id") id: Long, @Body request: MecanicoRequestDTO): MecanicoDTO

    @POST("api/clientes/login")
    suspend fun loginCliente(@Body request: LoginRequestDTO): ClienteDTO

    @POST("api/clientes/registro")
    suspend fun registerCliente(@Body request: RegisterRequestDTO): ClienteDTO

    @PUT("api/clientes/{id}")
    suspend fun updateCliente(@Path("id") id: Long, @Body request: ClienteRequestDTO): ClienteDTO
}