package com.carlafdez.autolog.presentation.screens.detailScreen

sealed interface DetailEvent {
    data object Retry : DetailEvent
    data object Refresh : DetailEvent
    data object CambiarEstado : DetailEvent
}
