package com.carlafdez.autolog.presentation.screens.loginScreen

import com.carlafdez.autolog.domain.model.TipoUsuario

sealed interface LoginEvent {
    data class OnEmailChanged(val email: String) : LoginEvent
    data class OnPasswordChanged(val password: String) : LoginEvent
    data class OnTipoUsuarioChanged(val tipo: TipoUsuario) : LoginEvent
    data object OnLoginClick : LoginEvent
}
