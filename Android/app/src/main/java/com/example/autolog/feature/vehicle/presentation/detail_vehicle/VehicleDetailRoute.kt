package com.example.autolog.feature.vehicle.presentation.detail_vehicle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VehicleDetailRoute(
    vehicleId: String,
    onBack: () -> Unit,
    viewModel: VehicleDetailViewModel = koinViewModel { parametersOf(vehicleId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val onAction: (VehicleDetailAction) -> Unit = { action ->
        when (action) {
            VehicleDetailAction.NavigateBack -> onBack()
        }
    }

    VehicleDetailScreen(
        uiState = uiState,
        onAction = onAction
    )
}
