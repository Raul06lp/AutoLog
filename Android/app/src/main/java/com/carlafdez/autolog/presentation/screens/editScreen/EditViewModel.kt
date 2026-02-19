package com.carlafdez.autolog.presentation.screens.editScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(
    private val vehiculoId: Long,
    private val repository: VehiculoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditUiState())
    val state = _state.asStateFlow()

    init {
        loadVehiculo()
    }

    fun onEvent(event: EditEvent) {
        when (event) {
            is EditEvent.MatriculaChanged -> _state.update { it.copy(matricula = event.value) }
            is EditEvent.MarcaChanged -> _state.update { it.copy(marca = event.value) }
            is EditEvent.ModeloChanged -> _state.update { it.copy(modelo = event.value) }
            is EditEvent.AnioChanged -> _state.update { it.copy(anio = event.value) }
            is EditEvent.ColorChanged -> _state.update { it.copy(color = event.value) }
            is EditEvent.KilometrajeChanged -> _state.update { it.copy(kilometraje = event.value) }
            is EditEvent.ObservacionesChanged -> _state.update { it.copy(observaciones = event.value) }
            is EditEvent.MedidasTomadasChanged -> _state.update { it.copy(medidasTomadas = event.value) }
            is EditEvent.ImagenSeleccionada -> _state.update { it.copy(nuevaImagenUri = event.uri) }
            EditEvent.GuardarClick -> guardar()
            EditEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
            EditEvent.ResetGuardado -> _state.update { it.copy(guardadoOk = false) } // Manejar el reset
        }
    }

    private fun loadVehiculo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, guardadoOk = false) }
            try {
                val v = repository.getVehiculoById(vehiculoId)
                if (v != null) {
                    _state.update {
                        it.copy(
                            vehiculoId = v.id,
                            idCliente = v.idCliente,
                            idMecanico = v.idMecanico,
                            matricula = v.matricula,
                            marca = v.marca,
                            modelo = v.modelo,
                            anio = v.anio.toString(),
                            color = v.color,
                            kilometraje = if (v.kilometraje == 0) "" else v.kilometraje.toString(),
                            observaciones = v.observaciones,
                            medidasTomadas = v.medidasTomadas,
                            imagenBase64Actual = v.imagenBase64,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(error = "Vehículo no encontrado", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cargar el vehículo", isLoading = false) }
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
        val idCliente = s.idCliente
        if (idCliente == null) {
            _state.update { it.copy(error = "Error: cliente no disponible") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.actualizarVehiculo(
                    id = vehiculoId,
                    matricula = s.matricula.uppercase(),
                    marca = s.marca,
                    modelo = s.modelo,
                    anio = anio,
                    color = s.color.ifBlank { null },
                    kilometraje = s.kilometraje.toIntOrNull(),
                    observaciones = s.observaciones.ifBlank { null },
                    medidasTomadas = s.medidasTomadas.ifBlank { null },
                    idCliente = idCliente,
                    idMecanico = s.idMecanico,
                    nuevaImagenUri = s.nuevaImagenUri
                )
                _state.update { it.copy(isLoading = false, guardadoOk = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }
}
