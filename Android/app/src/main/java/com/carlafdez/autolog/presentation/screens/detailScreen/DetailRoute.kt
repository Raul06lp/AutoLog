package com.carlafdez.autolog.presentation.screens.detailScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailRoute(
    vehiculoId: Long,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(
        key = "vehicle_$vehiculoId",
        parameters = { parametersOf(vehiculoId) }
    )
) {
    // Recargar datos cada vez que se entra a esta pantalla
    LaunchedEffect(Unit) {
        viewModel.onEvent(DetailEvent.Refresh)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onEditClick = onEditClick
    )
}
