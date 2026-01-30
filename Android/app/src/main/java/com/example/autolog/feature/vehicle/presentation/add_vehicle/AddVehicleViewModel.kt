package com.example.autolog.feature.vehicle.presentation.add_vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autolog.feature.vehicle.domain.model.Vehicle
import com.example.autolog.feature.vehicle.domain.usecase.AddVehicleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddVehicleViewModel(
    private val addVehiculo: AddVehicleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddVehicleUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddVehicleAction) {
        when(action) {
            is AddVehicleAction.AddCar -> onAddCar(action.vehicle)
            else -> {}
        }

    }

    private fun onAddCar(vehicle: Vehicle) {
        viewModelScope.launch {
            addVehiculo(vehicle)
        }
    }
}