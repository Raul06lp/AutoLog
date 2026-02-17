package com.carlafdez.autolog.presentation.screens.homeScreen

import com.carlafdez.autolog.domain.model.Vehiculo

data class HomeUiState (
    val userName: String = "",
    val vehicles: List<Vehiculo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)