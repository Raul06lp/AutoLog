package com.carlafdez.autolog.presentation.screens.addScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddRoute(
    onBack: () -> Unit,
    viewModel: AddViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}
