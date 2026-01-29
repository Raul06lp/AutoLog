package com.example.autolog.feature.vehicle.presentation.add_car

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autolog.feature.vehicle.domain.usecase.AddVehicleUseCase
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCarRoute(
    onBack: () -> Unit,
    viewModel: AddCarViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val onAction: (AddCarAction) -> Unit = { action ->
        when (action) {
            AddCarAction.NavigateBack -> {
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