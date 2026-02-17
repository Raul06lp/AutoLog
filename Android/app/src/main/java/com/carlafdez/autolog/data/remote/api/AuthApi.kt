package com.carlafdez.autolog.data.remote.api

import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.data.remote.dto.ClienteDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/mecanicos/login")
    suspend fun login(@Body request: LoginRequestDTO): ClienteDTO
}