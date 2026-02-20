package com.carlafdez.autolog.presentation.screens.homeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onVehicleClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    HomeScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is HomeEvent.VehicleClicked -> onVehicleClick(event.id)
                HomeEvent.OnAddClick -> onAddClick()
                HomeEvent.OnProfileClick -> onProfileClick()
                HomeEvent.Logout -> {
                    scope.launch {
                        viewModel.onEvent(HomeEvent.Logout)
                        onLogout()
                    }
                }
                else -> viewModel.onEvent(event)
            }
        }
    )
}
