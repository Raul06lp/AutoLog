package com.carlafdez.autolog.presentation.screens.homeScreen

sealed interface HomeEvent {
    data object Refresh : HomeEvent
    data class VehicleClicked(val id: Long) : HomeEvent
    data object Logout : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnAddClick : HomeEvent
}
