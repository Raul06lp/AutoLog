package com.carlafdez.autolog.presentation.screens.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel(
    private val vehiculoId: Long,
    private val repository: VehiculoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(VehicleDetailUiState())
    val state = _state.asStateFlow()

    init {
        loadVehiculo()
    }

    fun onEvent(event: VehicleDetailEvent) {
        when (event) {
            VehicleDetailEvent.Retry -> loadVehiculo()
            VehicleDetailEvent.Refresh -> loadVehiculo()
        }
    }

    private fun loadVehiculo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val vehiculo = repository.getVehiculoById(vehiculoId)
                if (vehiculo != null) {
                    _state.update { it.copy(vehiculo = vehiculo, isLoading = false) }
                } else {
                    _state.update { it.copy(error = "Vehículo no encontrado", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cargar los datos", isLoading = false) }
            }
        }
    }
}
