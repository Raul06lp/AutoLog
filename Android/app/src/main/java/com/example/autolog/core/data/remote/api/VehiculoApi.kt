package com.example.autolog.core.data.remote.api

import com.example.autolog.core.data.remote.dto.vehiculo.VehiculoDTO
import com.example.autolog.core.data.remote.dto.vehiculo.VehiculoRequestDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface VehiculoApi {
    
    /**
     * Crear vehículo
     * POST /api/vehiculos
     */
    @POST("api/vehiculos")
    suspend fun crearVehiculo(
        @Body requestDTO: VehiculoRequestDTO
    ): Response<VehiculoDTO>
    
    /**
     * Crear vehículo con imagen (multipart)
     * POST /api/vehiculos/con-imagen
     */
    @Multipart
    @POST("api/vehiculos/con-imagen")
    suspend fun crearVehiculoConImagen(
        @Part("matricula") matricula: String,
        @Part("marca") marca: String,
        @Part("modelo") modelo: String,
        @Part("anio") anio: Int,
        @Part("color") color: String?,
        @Part("kilometraje") kilometraje: Int?,
        @Part("observaciones") observaciones: String?,
        @Part("medidasTomadas") medidasTomadas: String?,
        @Part("estadoRevision") estadoRevision: String?,
        @Part("idCliente") idCliente: Long,
        @Part("idMecanico") idMecanico: Long?,
        @Part imagen: MultipartBody.Part?
    ): Response<VehiculoDTO>
    
    /**
     * Obtener todos los vehículos
     * GET /api/vehiculos
     */
    @GET("api/vehiculos")
    suspend fun obtenerTodos(): Response<List<VehiculoDTO>>
    
    /**
     * Obtener vehículo por ID
     * GET /api/vehiculos/{id}
     */
    @GET("api/vehiculos/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Long
    ): Response<VehiculoDTO>
    
    /**
     * Obtener imagen del vehículo
     * GET /api/vehiculos/{id}/imagen
     */
    @GET("api/vehiculos/{id}/imagen")
    suspend fun obtenerImagen(
        @Path("id") id: Long
    ): Response<ResponseBody>
    
    /**
     * Actualizar imagen del vehículo (Base64)
     * PUT /api/vehiculos/{id}/imagen
     */
    @PUT("api/vehiculos/{id}/imagen")
    suspend fun actualizarImagen(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): Response<VehiculoDTO>
    
    /**
     * Actualizar imagen del vehículo (Multipart)
     * PUT /api/vehiculos/{id}/imagen-file
     */
    @Multipart
    @PUT("api/vehiculos/{id}/imagen-file")
    suspend fun actualizarImagenFile(
        @Path("id") id: Long,
        @Part imagen: MultipartBody.Part
    ): Response<VehiculoDTO>
    
    /**
     * Obtener vehículo por matrícula
     * GET /api/vehiculos/matricula/{matricula}
     */
    @GET("api/vehiculos/matricula/{matricula}")
    suspend fun obtenerPorMatricula(
        @Path("matricula") matricula: String
    ): Response<VehiculoDTO>
    
    /**
     * Obtener vehículos por cliente
     * GET /api/vehiculos/cliente/{idCliente}
     */
    @GET("api/vehiculos/cliente/{idCliente}")
    suspend fun obtenerPorCliente(
        @Path("idCliente") idCliente: Long
    ): Response<List<VehiculoDTO>>
    
    /**
     * Obtener vehículos por mecánico
     * GET /api/vehiculos/mecanico/{idMecanico}
     */
    @GET("api/vehiculos/mecanico/{idMecanico}")
    suspend fun obtenerPorMecanico(
        @Path("idMecanico") idMecanico: Long
    ): Response<List<VehiculoDTO>>
    
    /**
     * Obtener vehículos por estado
     * GET /api/vehiculos/estado/{estado}
     */
    @GET("api/vehiculos/estado/{estado}")
    suspend fun obtenerPorEstado(
        @Path("estado") estado: String
    ): Response<List<VehiculoDTO>>
    
    /**
     * Obtener vehículos por cliente y estado
     * GET /api/vehiculos/cliente/{idCliente}/estado/{estado}
     */
    @GET("api/vehiculos/cliente/{idCliente}/estado/{estado}")
    suspend fun obtenerPorClienteYEstado(
        @Path("idCliente") idCliente: Long,
        @Path("estado") estado: String
    ): Response<List<VehiculoDTO>>
    
    /**
     * Obtener vehículos por mecánico y estado
     * GET /api/vehiculos/mecanico/{idMecanico}/estado/{estado}
     */
    @GET("api/vehiculos/mecanico/{idMecanico}/estado/{estado}")
    suspend fun obtenerPorMecanicoYEstado(
        @Path("idMecanico") idMecanico: Long,
        @Path("estado") estado: String
    ): Response<List<VehiculoDTO>>
    
    /**
     * Actualizar vehículo
     * PUT /api/vehiculos/{id}
     */
    @PUT("api/vehiculos/{id}")
    suspend fun actualizarVehiculo(
        @Path("id") id: Long,
        @Body requestDTO: VehiculoRequestDTO
    ): Response<VehiculoDTO>
    
    /**
     * Asignar mecánico a vehículo
     * PATCH /api/vehiculos/{id}/mecanico
     */
    @PATCH("api/vehiculos/{id}/mecanico")
    suspend fun asignarMecanico(
        @Path("id") id: Long,
        @Body body: Map<String, Long>
    ): Response<VehiculoDTO>
    
    /**
     * Actualizar estado del vehículo
     * PATCH /api/vehiculos/{id}/estado
     */
    @PATCH("api/vehiculos/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): Response<VehiculoDTO>
    
    /**
     * Eliminar vehículo
     * DELETE /api/vehiculos/{id}
     */
    @DELETE("api/vehiculos/{id}")
    suspend fun eliminarVehiculo(
        @Path("id") id: Long
    ): Response<Unit>
}
