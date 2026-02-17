package com.carlafdez.autolog.presentation.screens.detailScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VehicleDetailRoute(
    vehiculoId: Long,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: VehicleDetailViewModel = koinViewModel(
        key = "vehicle_$vehiculoId",  // ← esto es todo
        parameters = { parametersOf(vehiculoId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    VehicleDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onEditClick = onEditClick
    )
}
