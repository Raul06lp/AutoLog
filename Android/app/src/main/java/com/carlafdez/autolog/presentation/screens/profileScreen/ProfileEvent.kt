package com.carlafdez.autolog.presentation.screens.profileScreen

sealed interface ProfileEvent {
    data class NombreChanged(val value: String) : ProfileEvent
    data class EmailChanged(val value: String) : ProfileEvent
    data class ContrasenaActualChanged(val value: String) : ProfileEvent
    data class ContrasenaNuevaChanged(val value: String) : ProfileEvent
    data class ConfirmarContrasenaChanged(val value: String) : ProfileEvent
    data object ToggleCambiarContrasena : ProfileEvent
    data object GuardarCambios : ProfileEvent
    data object ErrorDismissed : ProfileEvent
    data object SuccessDismissed : ProfileEvent
}
