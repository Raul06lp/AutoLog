package com.example.autolog.core.data.remote.api

import com.example.autolog.core.data.remote.dto.cliente.ClienteDTO
import com.example.autolog.core.data.remote.dto.cliente.ClienteRequestDTO
import com.example.autolog.core.data.remote.dto.cliente.LoginDTO
import retrofit2.Response
import retrofit2.http.*

interface ClienteApi {
    
    /**
     * Registrar nuevo cliente
     * POST /api/clientes/registro
     */
    @POST("api/clientes/registro")
    suspend fun registrarCliente(
        @Body requestDTO: ClienteRequestDTO
    ): Response<ClienteDTO>
    
    /**
     * Login de cliente
     * POST /api/clientes/login
     */
    @POST("api/clientes/login")
    suspend fun login(
        @Body loginDTO: LoginDTO
    ): Response<ClienteDTO>
    
    /**
     * Obtener todos los clientes
     * GET /api/clientes
     */
    @GET("api/clientes")
    suspend fun obtenerTodos(): Response<List<ClienteDTO>>
    
    /**
     * Obtener cliente por ID
     * GET /api/clientes/{id}
     */
    @GET("api/clientes/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Long
    ): Response<ClienteDTO>
    
    /**
     * Obtener cliente por email
     * GET /api/clientes/email/{email}
     */
    @GET("api/clientes/email/{email}")
    suspend fun obtenerPorEmail(
        @Path("email") email: String
    ): Response<ClienteDTO>
    
    /**
     * Actualizar cliente
     * PUT /api/clientes/{id}
     */
    @PUT("api/clientes/{id}")
    suspend fun actualizarCliente(
        @Path("id") id: Long,
        @Body requestDTO: ClienteRequestDTO
    ): Response<ClienteDTO>
    
    /**
     * Eliminar cliente
     * DELETE /api/clientes/{id}
     */
    @DELETE("api/clientes/{id}")
    suspend fun eliminarCliente(
        @Path("id") id: Long
    ): Response<Unit>
}
