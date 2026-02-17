package com.carlafdez.autolog.domain.repository

import com.carlafdez.autolog.domain.model.Cliente

interface AuthRepository {
    suspend fun login(email: String, contrasena: String): Result<Cliente>
}