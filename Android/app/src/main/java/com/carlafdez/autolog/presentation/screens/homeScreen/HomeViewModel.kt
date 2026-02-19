package com.carlafdez.autolog.presentation.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.model.TipoUsuario
import com.carlafdez.autolog.domain.model.Usuario
import com.carlafdez.autolog.domain.repository.AuthRepository
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: VehiculoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

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
                val usuario = authRepository.getUsuario().first()
                if (usuario == null) {
                    _state.update { it.copy(error = "Sesión no encontrada", isLoading = false) }
                    return@launch
                }

                val allVehicles = when (usuario) {
                    is Usuario.MecanicoUsuario -> repository.getVehiclesByMecanico(usuario.id)
                    is Usuario.ClienteUsuario -> repository.getVehiclesByCliente(usuario.id)
                }

                // Filtrar según el tipo de usuario
                val vehicles = when (usuario.tipo) {
                    TipoUsuario.MECANICO -> allVehicles.filter { it.estadoRevision != "finalizado" }
                    TipoUsuario.CLIENTE -> allVehicles // Clientes ven TODOS sus vehículos
                }

                _state.update { it.copy(vehicles = vehicles, isLoading = false, usuario = usuario) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cargar los vehículos", isLoading = false) }
            }
        }
    }
}
