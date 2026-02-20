package com.carlafdez.autolog.domain.repository

import com.carlafdez.autolog.domain.model.TipoUsuario
import com.carlafdez.autolog.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, contrasena: String, tipo: TipoUsuario): Result<Usuario>
    suspend fun register(nombre: String, email: String, contrasena: String, tipo: TipoUsuario): Result<Usuario>
    suspend fun verificarContrasena(email: String, contrasena: String, tipo: TipoUsuario): Result<Unit>
    suspend fun updateProfile(id: Long, nombre: String, email: String, contrasena: String, tipo: TipoUsuario): Result<Usuario>
    fun getUsuario(): Flow<Usuario?>
    suspend fun logout()
}