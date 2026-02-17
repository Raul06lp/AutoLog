package com.carlafdez.autolog.data.repository

import com.carlafdez.autolog.data.remote.api.AuthApi
import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.domain.model.Cliente
import com.carlafdez.autolog.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun login(email: String, contrasena: String): Result<Cliente> {
        return try {
            val response = api.login(LoginRequestDTO(email, contrasena))
            Result.success(Cliente(
                id = response.idCliente,
                nombre = response.nombre,
                email = response.email
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}