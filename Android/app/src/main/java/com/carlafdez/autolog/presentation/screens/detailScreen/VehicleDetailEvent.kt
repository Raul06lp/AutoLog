package com.carlafdez.autolog.presentation.screens.detailScreen

sealed interface VehicleDetailEvent {
    data object Retry : VehicleDetailEvent
}
