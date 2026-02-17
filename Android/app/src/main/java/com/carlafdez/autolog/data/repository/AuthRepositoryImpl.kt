package com.carlafdez.autolog.data.repository

import com.carlafdez.autolog.data.local.SessionManager
import com.carlafdez.autolog.data.remote.api.AuthApi
import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.data.remote.dto.RegisterRequestDTO
import com.carlafdez.autolog.domain.model.Mecanico
import com.carlafdez.autolog.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, contrasena: String): Result<Mecanico> {
        return try {
            val response = api.login(LoginRequestDTO(email, contrasena))
            val mecanico = Mecanico(id = response.idMecanico, nombre = response.nombre, email = response.email)
            sessionManager.saveSession(mecanico.id, mecanico.nombre)
            Result.success(mecanico)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(nombre: String, email: String, contrasena: String): Result<Mecanico> {
        return try {
            val response = api.register(RegisterRequestDTO(nombre, email, contrasena))
            val mecanico = Mecanico(id = response.idMecanico, nombre = response.nombre, email = response.email)
            sessionManager.saveSession(mecanico.id, mecanico.nombre)
            Result.success(mecanico)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMecanicoId(): Flow<Long?> = sessionManager.mecanicoId
    override fun getMecanicoNombre(): Flow<String?> = sessionManager.mecanicoNombre
}
