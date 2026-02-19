package com.carlafdez.autolog.presentation.screens.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.repository.AuthRepository
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val vehiculoId: Long,
    private val repository: VehiculoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state = _state.asStateFlow()

    init {
        loadVehiculo()
        loadUsuario()
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            DetailEvent.Retry -> loadVehiculo()
            DetailEvent.Refresh -> loadVehiculo()
            DetailEvent.CambiarEstado -> cambiarEstado()
        }
    }

    private fun loadUsuario() {
        viewModelScope.launch {
            val usuario = authRepository.getUsuario().first()
            _state.update { it.copy(usuario = usuario) }
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

    private fun cambiarEstado() {
        val vehiculo = _state.value.vehiculo ?: return
        val nuevoEstado = when (vehiculo.estadoRevision) {
            "pendiente" -> "en_revision"
            "en_revision" -> "finalizado"
            else -> return // Si ya está finalizado, no hacer nada
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                repository.cambiarEstado(vehiculoId, nuevoEstado)
                loadVehiculo() // Recargar para mostrar el nuevo estado
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cambiar el estado", isLoading = false) }
            }
        }
    }
}
