package com.carlafdez.autolog.presentation.screens.registerScreen

import com.carlafdez.autolog.domain.model.TipoUsuario

sealed interface RegisterEvent {
    data class OnNombreChanged(val value: String) : RegisterEvent
    data class OnEmailChanged(val value: String) : RegisterEvent
    data class OnPasswordChanged(val value: String) : RegisterEvent
    data class OnConfirmPasswordChanged(val value: String) : RegisterEvent
    data class OnTipoUsuarioChanged(val tipo: TipoUsuario) : RegisterEvent
    data object OnRegisterClick : RegisterEvent
}
