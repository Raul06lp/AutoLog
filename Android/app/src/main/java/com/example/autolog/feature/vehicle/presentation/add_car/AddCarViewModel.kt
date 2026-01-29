package com.example.autolog.feature.vehicle.presentation.add_car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autolog.feature.vehicle.domain.model.Vehicle
import com.example.autolog.feature.vehicle.domain.usecase.AddVehicleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddCarViewModel(
    private val addVehiculo: AddVehicleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCarUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddCarAction) {
        when(action) {
            is AddCarAction.AddCar -> onAddCar(action.vehicle)
            else -> {}
        }

    }

    private fun onAddCar(vehicle: Vehicle) {
        viewModelScope.launch {
            addVehiculo(vehicle)
        }
    }
}