package com.carlafdez.autolog.data.repository

import com.carlafdez.autolog.data.local.SessionManager
import com.carlafdez.autolog.data.remote.api.AuthApi
import com.carlafdez.autolog.data.remote.dto.LoginRequestDTO
import com.carlafdez.autolog.data.remote.dto.RegisterRequestDTO
import com.carlafdez.autolog.domain.model.TipoUsuario
import com.carlafdez.autolog.domain.model.Usuario
import com.carlafdez.autolog.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, contrasena: String, tipo: TipoUsuario): Result<Usuario> {
        return try {
            val usuario = when (tipo) {
                TipoUsuario.MECANICO -> {
                    val response = api.loginMecanico(LoginRequestDTO(email, contrasena))
                    Usuario.MecanicoUsuario(
                        id = response.idMecanico,
                        nombre = response.nombre,
                        email = response.email,
                        cantidadVehiculos = response.cantidadVehiculos ?: 0
                    )
                }
                TipoUsuario.CLIENTE -> {
                    val response = api.loginCliente(LoginRequestDTO(email, contrasena))
                    Usuario.ClienteUsuario(
                        id = response.idCliente,
                        nombre = response.nombre,
                        email = response.email,
                        cantidadVehiculos = response.cantidadVehiculos ?: 0
                    )
                }
            }
            sessionManager.saveSession(usuario.id, usuario.nombre, usuario.email, tipo)
            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(nombre: String, email: String, contrasena: String, tipo: TipoUsuario): Result<Usuario> {
        return try {
            val usuario = when (tipo) {
                TipoUsuario.MECANICO -> {
                    val response = api.registerMecanico(RegisterRequestDTO(nombre, email, contrasena))
                    Usuario.MecanicoUsuario(
                        id = response.idMecanico,
                        nombre = response.nombre,
                        email = response.email,
                        cantidadVehiculos = response.cantidadVehiculos ?: 0
                    )
                }
                TipoUsuario.CLIENTE -> {
                    val response = api.registerCliente(RegisterRequestDTO(nombre, email, contrasena))
                    Usuario.ClienteUsuario(
                        id = response.idCliente,
                        nombre = response.nombre,
                        email = response.email,
                        cantidadVehiculos = response.cantidadVehiculos ?: 0
                    )
                }
            }
            sessionManager.saveSession(usuario.id, usuario.nombre, usuario.email, tipo)
            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUsuario(): Flow<Usuario?> = sessionManager.usuario

    override suspend fun logout() {
        sessionManager.clearSession()
    }
}
