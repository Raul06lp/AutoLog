package com.example.autolog.feature.vehicle.presentation.detail_vehicle

import com.example.autolog.feature.vehicle.domain.model.Vehicle

data class VehicleDetailUiState(
    val vehicle: Vehicle? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)