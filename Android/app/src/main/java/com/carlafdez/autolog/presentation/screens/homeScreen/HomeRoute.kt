package com.carlafdez.autolog.presentation.screens.homeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onVehicleClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is HomeEvent.VehicleClicked -> onVehicleClick(event.id)
                else -> viewModel.onEvent(event)
            }
        },
        onAddClick = onAddClick
    )
}