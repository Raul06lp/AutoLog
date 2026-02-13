package com.example.autolog.presentation.screens.edit_vehicle


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VehicleEditRoute(
    vehicleId: String,
    onBack: () -> Unit,
    viewModel: EditVehicleViewModel = koinViewModel { parametersOf(vehicleId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navegar atrás cuando se guarda exitosamente
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onBack()
        }
    }

    val onAction: (VehicleEditAction) -> Unit = { action ->
        when (action) {
            VehicleEditAction.NavigateBack -> onBack()
            else -> viewModel.onAction(action)
        }
    }

    VehicleEditRoute(
        vehicleId = vehicleId,
        onBack = {  }
    )
}