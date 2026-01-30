package com.example.autolog.feature.vehicle.presentation.add_vehicle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCarRoute(
    onBack: () -> Unit,
    viewModel: AddVehicleViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val onAction: (AddVehicleAction) -> Unit = { action ->
        when (action) {
            AddVehicleAction.NavigateBack -> {
                onBack()
            }
            else -> viewModel.onAction((action))
        }
    }

    CarAddScreen(
        uiState,
        onAction = onAction
    )

}