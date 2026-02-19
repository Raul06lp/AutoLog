package com.carlafdez.autolog.presentation.screens.addScreen

import android.net.Uri
import com.carlafdez.autolog.domain.model.Cliente

data class AddUiState(
    val matricula: String = "",
    val marca: String = "",
    val modelo: String = "",
    val anio: String = "",
    val color: String = "",
    val kilometraje: String = "",
    val observaciones: String = "",
    val clienteSeleccionado: Cliente? = null,
    val clientes: List<Cliente> = emptyList(),
    val isLoadingClientes: Boolean = false,
    val imagenUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val guardadoOk: Boolean = false
)
