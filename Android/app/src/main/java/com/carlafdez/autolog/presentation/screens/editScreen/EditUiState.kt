package com.carlafdez.autolog.presentation.screens.editScreen

import android.net.Uri

data class EditUiState(
    val vehiculoId: Long = 0,
    val idCliente: Long? = null,
    val idMecanico: Long? = null,
    val matricula: String = "",
    val marca: String = "",
    val modelo: String = "",
    val anio: String = "",
    val color: String = "",
    val kilometraje: String = "",
    val observaciones: String = "",
    val medidasTomadas: String = "",
    val imagenBase64Actual: String? = null,
    val nuevaImagenUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val guardadoOk: Boolean = false
)
