package com.carlafdez.autolog.presentation.screens.addScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.model.Usuario
import com.carlafdez.autolog.domain.repository.AuthRepository
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddViewModel(
    private val repository: VehiculoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddUiState())
    val state = _state.asStateFlow()

    init {
        loadClientes()
    }

    fun onEvent(event: AddEvent) {
        when (event) {
            is AddEvent.MatriculaChanged -> _state.update { it.copy(matricula = event.value) }
            is AddEvent.MarcaChanged -> _state.update { it.copy(marca = event.value) }
            is AddEvent.ModeloChanged -> _state.update { it.copy(modelo = event.value) }
            is AddEvent.AnioChanged -> _state.update { it.copy(anio = event.value) }
            is AddEvent.ColorChanged -> _state.update { it.copy(color = event.value) }
            is AddEvent.KilometrajeChanged -> _state.update { it.copy(kilometraje = event.value) }
            is AddEvent.ObservacionesChanged -> _state.update { it.copy(observaciones = event.value) }
            is AddEvent.ClienteSeleccionado -> _state.update { it.copy(clienteSeleccionado = event.cliente) }
            is AddEvent.ImagenSeleccionada -> _state.update { it.copy(imagenUri = event.uri) }
            AddEvent.GuardarClick -> guardar()
            AddEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
            AddEvent.ResetGuardado -> _state.update { it.copy(guardadoOk = false) }
        }
    }

    private fun loadClientes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingClientes = true) }
            try {
                val clientes = repository.getClientes()
                _state.update { it.copy(clientes = clientes, isLoadingClientes = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoadingClientes = false) }
            }
        }
    }

    private fun guardar() {
        val s = _state.value
        if (s.matricula.isBlank() || s.marca.isBlank() || s.modelo.isBlank() || s.anio.isBlank()) {
            _state.update { it.copy(error = "Matrícula, marca, modelo y año son obligatorios") }
            return
        }
        val anio = s.anio.toIntOrNull()
        if (anio == null) {
            _state.update { it.copy(error = "El año debe ser un número válido") }
            return
        }
        if (s.clienteSeleccionado == null) {
            _state.update { it.copy(error = "Debes seleccionar un cliente") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Obtener el usuario logueado (debe ser mecánico)
                val usuario = authRepository.getUsuario().first()
                val mecanicoId = when (usuario) {
                    is Usuario.MecanicoUsuario -> usuario.id
                    else -> {
                        _state.update { it.copy(isLoading = false, error = "Error: usuario no autorizado") }
                        return@launch
                    }
                }

                repository.crearVehiculo(
                    matricula = s.matricula.uppercase(),
                    marca = s.marca,
                    modelo = s.modelo,
                    anio = anio,
                    color = s.color.ifBlank { null },
                    kilometraje = s.kilometraje.toIntOrNull(),
                    observaciones = s.observaciones.ifBlank { null },
                    idCliente = s.clienteSeleccionado.id,
                    idMecanico = mecanicoId,
                    imagenUri = s.imagenUri
                )
                _state.update { it.copy(isLoading = false, guardadoOk = true) }
            } catch (e: com.carlafdez.autolog.data.remote.ApiException) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }
}
