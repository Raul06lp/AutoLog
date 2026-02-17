package com.carlafdez.autolog.domain.repository

import com.carlafdez.autolog.domain.model.Mecanico
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, contrasena: String): Result<Mecanico>
    suspend fun register(nombre: String, email: String, contrasena: String): Result<Mecanico>
    fun getMecanicoId(): Flow<Long?>
    fun getMecanicoNombre(): Flow<String?>
}