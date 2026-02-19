package com.carlafdez.autolog.presentation.screens.detailScreen

import com.carlafdez.autolog.domain.model.Usuario
import com.carlafdez.autolog.domain.model.Vehiculo

data class VehicleDetailUiState(
    val vehiculo: Vehiculo? = null,
    val usuario: Usuario? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
