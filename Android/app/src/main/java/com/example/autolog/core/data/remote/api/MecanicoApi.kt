package com.example.autolog.core.data.remote.api

import com.example.autolog.core.data.remote.dto.cliente.LoginDTO
import com.example.autolog.core.data.remote.dto.mecanico.MecanicoDTO
import com.example.autolog.core.data.remote.dto.mecanico.MecanicoRequestDTO
import retrofit2.Response
import retrofit2.http.*

interface MecanicoApi {
    
    /**
     * Registrar nuevo mecánico
     * POST /api/mecanicos/registro
     */
    @POST("api/mecanicos/registro")
    suspend fun registrarMecanico(
        @Body requestDTO: MecanicoRequestDTO
    ): Response<MecanicoDTO>
    
    /**
     * Login de mecánico
     * POST /api/mecanicos/login
     */
    @POST("api/mecanicos/login")
    suspend fun login(
        @Body loginDTO: LoginDTO
    ): Response<MecanicoDTO>
    
    /**
     * Obtener todos los mecánicos
     * GET /api/mecanicos
     */
    @GET("api/mecanicos")
    suspend fun obtenerTodos(): Response<List<MecanicoDTO>>
    
    /**
     * Obtener mecánico por ID
     * GET /api/mecanicos/{id}
     */
    @GET("api/mecanicos/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Long
    ): Response<MecanicoDTO>
    
    /**
     * Obtener mecánico por email
     * GET /api/mecanicos/email/{email}
     */
    @GET("api/mecanicos/email/{email}")
    suspend fun obtenerPorEmail(
        @Path("email") email: String
    ): Response<MecanicoDTO>
    
    /**
     * Actualizar mecánico
     * PUT /api/mecanicos/{id}
     */
    @PUT("api/mecanicos/{id}")
    suspend fun actualizarMecanico(
        @Path("id") id: Long,
        @Body requestDTO: MecanicoRequestDTO
    ): Response<MecanicoDTO>
    
    /**
     * Eliminar mecánico
     * DELETE /api/mecanicos/{id}
     */
    @DELETE("api/mecanicos/{id}")
    suspend fun eliminarMecanico(
        @Path("id") id: Long
    ): Response<Unit>
}
