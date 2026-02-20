package com.carlafdez.autolog.presentation.screens.registerScreen

import com.carlafdez.autolog.domain.model.TipoUsuario

data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val tipoUsuario: TipoUsuario = TipoUsuario.MECANICO,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccessful: Boolean = false
)
