package com.carlafdez.autolog.presentation.screens.profileScreen

import com.carlafdez.autolog.domain.model.Usuario

data class ProfileUiState(
    val usuario: Usuario? = null,
    val nombre: String = "",
    val email: String = "",
    val contrasenaActual: String = "",
    val contrasenaNueva: String = "",
    val confirmarContrasena: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false,
    val mostrarCambiarContrasena: Boolean = false
)
