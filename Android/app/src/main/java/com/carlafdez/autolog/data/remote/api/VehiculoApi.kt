package com.carlafdez.autolog.data.remote.api

import com.carlafdez.autolog.data.remote.dto.ClienteDTO
import com.carlafdez.autolog.data.remote.dto.VehiculoDTO
import com.carlafdez.autolog.data.remote.dto.VehiculoRequestDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface VehiculoApi {

    @GET("api/vehiculos/mecanico/{idMecanico}")
    suspend fun getVehiculosByMecanico(@Path("idMecanico") idMecanico: Long): List<VehiculoDTO>

    @GET("api/vehiculos/cliente/{idCliente}")
    suspend fun getVehiculosByCliente(@Path("idCliente") idCliente: Long): List<VehiculoDTO>

    @GET("api/vehiculos/{id}")
    suspend fun getVehiculoById(@Path("id") id: Long): VehiculoDTO

    @POST("api/vehiculos")
    suspend fun crearVehiculo(@Body request: VehiculoRequestDTO): VehiculoDTO

    @Multipart
    @POST("api/vehiculos/con-imagen")
    suspend fun crearVehiculoConImagen(
        @Part("matricula") matricula: RequestBody,
        @Part("marca") marca: RequestBody,
        @Part("modelo") modelo: RequestBody,
        @Part("anio") anio: RequestBody,
        @Part("color") color: RequestBody?,
        @Part("kilometraje") kilometraje: RequestBody?,
        @Part("observaciones") observaciones: RequestBody?,
        @Part("estadoRevision") estadoRevision: RequestBody,
        @Part("idCliente") idCliente: RequestBody,
        @Part("idMecanico") idMecanico: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): VehiculoDTO

    @PUT("api/vehiculos/{id}")
    suspend fun actualizarVehiculo(
        @Path("id") id: Long,
        @Body request: VehiculoRequestDTO
    ): VehiculoDTO

    @Multipart
    @PUT("api/vehiculos/{id}/imagen-file")
    suspend fun actualizarImagenFile(
        @Path("id") id: Long,
        @Part imagen: MultipartBody.Part
    ): VehiculoDTO

    @PATCH("api/vehiculos/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): VehiculoDTO

    @GET("api/clientes")
    suspend fun getClientes(): List<ClienteDTO>
}
