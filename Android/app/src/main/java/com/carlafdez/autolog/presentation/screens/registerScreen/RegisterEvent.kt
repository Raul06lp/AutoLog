package com.carlafdez.autolog.presentation.screens.registerScreen

sealed interface RegisterEvent {
    data class OnNombreChanged(val value: String) : RegisterEvent
    data class OnEmailChanged(val value: String) : RegisterEvent
    data class OnPasswordChanged(val value: String) : RegisterEvent
    data class OnConfirmPasswordChanged(val value: String) : RegisterEvent
    data object OnRegisterClick : RegisterEvent
}