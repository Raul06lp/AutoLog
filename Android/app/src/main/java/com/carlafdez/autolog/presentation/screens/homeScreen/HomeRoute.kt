package com.carlafdez.autolog.presentation.screens.homeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onVehicleClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    HomeScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is HomeEvent.VehicleClicked -> onVehicleClick(event.id)
                is HomeEvent.Logout -> {
                    onLogout()
                    viewModel.onEvent(event)
                }
                else -> viewModel.onEvent(event)
            }
        },
        onAddClick = onAddClick
    )
}