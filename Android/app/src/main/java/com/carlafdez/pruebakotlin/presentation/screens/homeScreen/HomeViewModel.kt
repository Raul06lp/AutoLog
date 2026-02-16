package com.carlafdez.pruebakotlin.presentation.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.pruebakotlin.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: VehiculoRepository) : ViewModel(){
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    // TODO: Obtener del usuario logueado
    private val mecanicoId = 2L

    init {
        loadVehicles()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Refresh -> loadVehicles()
            is HomeEvent.VehicleClicked -> { /* navegación en NavigationRoot */ }
        }
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val vehicles = repository.getVehiclesByMecanico(mecanicoId)
                _state.update { it.copy(vehicles = vehicles, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cargar los vehículos", isLoading = false) }
            }
        }
    }
}