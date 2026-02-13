package com.example.autolog.presentation.screens.edit_vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autolog.domain.repository.VehiculoRepository
import com.example.autolog.domain.usecase.EditVehicleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditVehicleViewModel(
    private val vehicleId: String,
    private val repository: VehiculoRepository,
    private val editCarUseCase: EditVehicleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleEditUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: VehicleEditAction) {
        when(action) {
            is VehicleEditAction.SaveVehicle -> saveVehicle()
            is VehicleEditAction.UpdateImageUri -> updateImageUri(action.uri)
            is VehicleEditAction.UpdateClientName -> updateClientName(action.name)
            is VehicleEditAction.UpdateMarca -> updateMarca(action.marca)
            is VehicleEditAction.UpdateModelo -> updateModelo(action.modelo)
            is VehicleEditAction.UpdateMatricula -> updateMatricula(action.matricula)
            is VehicleEditAction.UpdateYear -> updateYear(action.year)
            is VehicleEditAction.UpdateKilometros -> updateKilometros(action.kilometros)
            is VehicleEditAction.UpdateColor -> updateColor(action.color)
            is VehicleEditAction.UpdateObservaciones -> updateObservaciones(action.observaciones)
            is VehicleEditAction.UpdateMedidas -> updateMedidas(action.medidas)
            VehicleEditAction.NavigateBack -> {
                // Manejado en el Route
            }
        }
    }
    private fun updateImageUri(uri: String) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    private fun updateClientName(name: String) {
        _uiState.update { it.copy(clientName = name) }
    }

    private fun updateMarca(marca: String) {
        _uiState.update { it.copy(marca = marca) }
    }

    private fun updateModelo(modelo: String) {
        _uiState.update { it.copy(modelo = modelo) }
    }

    private fun updateMatricula(matricula: String) {
        _uiState.update { it.copy(matricula = matricula) }
    }

    private fun updateYear(year: String) {
        _uiState.update { it.copy(year = year) }
    }

    private fun updateKilometros(kilometros: String) {
        _uiState.update { it.copy(kilometros = kilometros) }
    }

    private fun updateColor(color: String) {
        _uiState.update { it.copy(color = color) }
    }

    private fun updateObservaciones(observaciones: String) {
        _uiState.update { it.copy(observaciones = observaciones) }
    }

    private fun updateMedidas(medidas: String) {
        _uiState.update { it.copy(medidas = medidas) }
    }

    private fun saveVehicle() {
        val currentState = _uiState.value
        val originalVehicle = currentState.originalVehicle ?: return


        if (currentState.clientName.isBlank() ||
            currentState.marca.isBlank() ||
            currentState.modelo.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Por favor completa los campos obligatorios")
            }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val updatedVehicle = originalVehicle.copy(
                    imagen = null,
                    cliente = originalVehicle.cliente.copy(name = currentState.clientName),
                    marca = currentState.marca,
                    modelo = currentState.modelo,
                    matricula = currentState.matricula,
                    year = currentState.year.toIntOrNull() ?: originalVehicle.year,
                    kilometros = currentState.kilometros,
                    color = currentState.color,
                    observaciones = currentState.observaciones
                )

                repository.updateVehicle(updatedVehicle)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error al actualizar vehículo"
                    )
                }
            }
        }
    }
}
