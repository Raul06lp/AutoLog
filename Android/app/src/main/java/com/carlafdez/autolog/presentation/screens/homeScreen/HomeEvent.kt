package com.carlafdez.autolog.presentation.screens.homeScreen

interface HomeEvent {
    data object Refresh : HomeEvent
    data class VehicleClicked(val id: Long) : HomeEvent
}