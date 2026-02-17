package com.carlafdez.autolog.presentation.screens.editScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditRoute(
    vehiculoId: Long,
    onBack: () -> Unit,
    viewModel: EditViewModel = koinViewModel(
        key = "edit_$vehiculoId",
        parameters = { parametersOf(vehiculoId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}
