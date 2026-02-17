package com.carlafdez.autolog.data.remote.api

import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.data.remote.dto.MecanicoDTO
import com.carlafdez.autolog.data.remote.dto.RegisterRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/mecanicos/login")
    suspend fun login(@Body request: LoginRequestDTO): MecanicoDTO

    @POST("api/mecanicos/registro")
    suspend fun register(@Body request: RegisterRequestDTO): MecanicoDTO
}
