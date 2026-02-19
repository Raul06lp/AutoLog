package com.carlafdez.autolog.data.repository

import android.content.Context
import android.net.Uri
import com.carlafdez.autolog.data.remote.ApiException
import com.carlafdez.autolog.data.remote.api.VehiculoApi
import com.carlafdez.autolog.data.remote.dto.ErrorResponseDTO
import com.carlafdez.autolog.data.remote.dto.VehiculoRequestDTO
import com.carlafdez.autolog.data.remote.mapper.toVehicle
import com.carlafdez.autolog.domain.model.Cliente
import com.carlafdez.autolog.domain.model.Vehiculo
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class VehiculoRepositoryImpl(
    private val api: VehiculoApi,
    private val context: Context
) : VehiculoRepository {

    override suspend fun getVehiclesByMecanico(idMecanico: Long): List<Vehiculo> =
        api.getVehiculosByMecanico(idMecanico).map { it.toVehicle() }

    override suspend fun getVehiclesByCliente(idCliente: Long): List<Vehiculo> =
        api.getVehiculosByCliente(idCliente).map { it.toVehicle() }

    override suspend fun getVehiculoById(id: Long): Vehiculo? = try {
        api.getVehiculoById(id).toVehicle()
    } catch (e: Exception) { null }

    override suspend fun crearVehiculo(
        matricula: String, marca: String, modelo: String, anio: Int,
        color: String?, kilometraje: Int?, observaciones: String?,
        idCliente: Long, idMecanico: Long, imagenUri: Uri?
    ): Vehiculo {
        try {
            return if (imagenUri != null) {
                val imagenPart = prepareImagePart(imagenUri)
                api.crearVehiculoConImagen(
                    matricula = matricula.toPlainBody(),
                    marca = marca.toPlainBody(),
                    modelo = modelo.toPlainBody(),
                    anio = anio.toString().toPlainBody(),
                    color = color?.toPlainBody(),
                    kilometraje = kilometraje?.toString()?.toPlainBody(),
                    observaciones = observaciones?.toPlainBody(),
                    estadoRevision = "Pendiente".toPlainBody(),
                    idCliente = idCliente.toString().toPlainBody(),
                    idMecanico = idMecanico.toString().toPlainBody(),
                    imagen = imagenPart
                ).toVehicle()
            } else {
                api.crearVehiculo(
                    VehiculoRequestDTO(
                        matricula = matricula, marca = marca, modelo = modelo, anio = anio,
                        color = color, kilometraje = kilometraje, observaciones = observaciones,
                        medidasTomadas = null, idCliente = idCliente, idMecanico = idMecanico,
                        estadoRevision = "Pendiente"
                    )
                ).toVehicle()
            }
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            throw ApiException(e.code(), errorMessage)
        }
    }


    override suspend fun actualizarVehiculo(
        id: Long,
        matricula: String,
        marca: String,
        modelo: String,
        anio: Int,
        color: String?,
        kilometraje: Int?,
        observaciones: String?,
        medidasTomadas: String?,
        idCliente: Long,
        idMecanico: Long?,
        nuevaImagenUri: Uri?
    ): Vehiculo {
        val vehiculoActual = getVehiculoById(id)
        val estadoActual = vehiculoActual?.estadoRevision ?: "pendiente"

        try {
            val vehiculoActualizado = api.actualizarVehiculo(
                id,
                VehiculoRequestDTO(
                    matricula = matricula, marca = marca, modelo = modelo, anio = anio,
                    color = color, kilometraje = kilometraje, observaciones = observaciones,
                    medidasTomadas = medidasTomadas,
                    estadoRevision = estadoActual,
                    idCliente = idCliente, idMecanico = idMecanico
                )
            ).toVehicle()

            if (nuevaImagenUri != null) {
                val imagenPart = prepareImagePart(nuevaImagenUri)
                if (imagenPart != null) {
                    return api.actualizarImagenFile(id, imagenPart).toVehicle()
                }
            }

            return vehiculoActualizado
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            throw ApiException(e.code(), errorMessage)
        }
    }

    override suspend fun cambiarEstado(id: Long, nuevoEstado: String) {
        api.cambiarEstado(id, mapOf("estadoRevision" to nuevoEstado))
    }

    override suspend fun getClientes(): List<Cliente> =
        api.getClientes().map { Cliente(id = it.idCliente, nombre = it.nombre, email = it.email) }

    private fun prepareImagePart(uri: Uri): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { inputStream.copyTo(it) }
            val requestBody = tempFile.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imagen", tempFile.name, requestBody)
        } catch (e: Exception) { null }
    }

    private fun String.toPlainBody() = toRequestBody("text/plain".toMediaTypeOrNull())

    private fun extractErrorMessage(exception: HttpException): String {
        return try {
            val errorBody = exception.response()?.errorBody()?.string()
            if (errorBody != null) {
                val errorResponse = Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                // Spring Boot devuelve mensajes de validación en diferentes campos
                when {
                    !errorResponse.message.isNullOrBlank() -> errorResponse.message
                    errorResponse.status == 400 -> {
                        // Para errores de validación, intentamos extraer info del error
                        val anioPattern = "anio|año".toRegex(RegexOption.IGNORE_CASE)
                        if (errorBody.contains(anioPattern)) {
                            "El año debe estar entre 1900 y 2027"
                        } else {
                            "Datos no válidos. Verifica los campos obligatorios"
                        }
                    }
                    else -> errorResponse.error ?: "Error al guardar los cambios"
                }
            } else {
                "Error al guardar los cambios (${exception.code()})"
            }
        } catch (e: Exception) {
            "Error al guardar los cambios"
        }
    }
}