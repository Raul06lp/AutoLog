package com.carlafdez.pruebakotlin.presentation.screens.homeScreen

import com.carlafdez.pruebakotlin.domain.model.Vehiculo

data class HomeUiState (
    val vehicles: List<Vehiculo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)