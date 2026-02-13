package com.example.autolog.presentation.screens.detail_vehicle

import com.example.autolog.domain.model.Vehicle

data class VehicleDetailUiState(
    val vehicle: Vehicle? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)