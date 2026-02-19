package com.carlafdez.autolog.presentation.screens.loginScreen

import com.carlafdez.autolog.domain.model.TipoUsuario

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val tipoUsuario: TipoUsuario = TipoUsuario.MECANICO,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false
)
