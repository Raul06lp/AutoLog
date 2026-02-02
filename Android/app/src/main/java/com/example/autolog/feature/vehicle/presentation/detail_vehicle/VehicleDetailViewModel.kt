package com.example.autolog.feature.vehicle.presentation.detail_vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autolog.feature.vehicle.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel(
    private val vehicleId: String,
    private val repository: VehiculoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadVehicle()
    }

    fun onAction(action: VehicleDetailAction) {
        when (action) {
            VehicleDetailAction.NavigateBack -> {
                // Manejado en el Route
            }
        }
    }

    private fun loadVehicle() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val vehicle = repository.getVehicleById(vehicleId)
                _uiState.update {
                    it.copy(
                        vehicle = vehicle,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar el vehículo"
                    )
                }
            }
        }
    }
}