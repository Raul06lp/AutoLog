package com.carlafdez.autolog.presentation.screens.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    init {
        loadUserData()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.NombreChanged -> _state.update { it.copy(nombre = event.value, error = null) }
            is ProfileEvent.EmailChanged -> _state.update { it.copy(email = event.value, error = null) }
            is ProfileEvent.ContrasenaActualChanged -> _state.update { it.copy(contrasenaActual = event.value, error = null) }
            is ProfileEvent.ContrasenaNuevaChanged -> _state.update { it.copy(contrasenaNueva = event.value, error = null) }
            is ProfileEvent.ConfirmarContrasenaChanged -> _state.update { it.copy(confirmarContrasena = event.value, error = null) }
            ProfileEvent.ToggleCambiarContrasena -> _state.update {
                it.copy(
                    mostrarCambiarContrasena = !it.mostrarCambiarContrasena,
                    contrasenaActual = "",
                    contrasenaNueva = "",
                    confirmarContrasena = "",
                    error = null
                )
            }
            ProfileEvent.GuardarCambios -> guardarCambios()
            ProfileEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
            ProfileEvent.SuccessDismissed -> _state.update { it.copy(updateSuccess = false) }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Usamos firstOrNull para evitar excepciones si el flow no emite
                val usuario = authRepository.getUsuario().firstOrNull()
                if (usuario != null) {
                    _state.update {
                        it.copy(
                            usuario = usuario,
                            nombre = usuario.nombre,
                            email = usuario.email,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(error = "No se pudo cargar la información del usuario", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cargar datos: ${e.message}", isLoading = false) }
            }
        }
    }

    private fun guardarCambios() {
        val s = _state.value
        val usuario = s.usuario ?: return

        if (s.nombre.isBlank()) {
            _state.update { it.copy(error = "El nombre no puede estar vacío") }
            return
        }
        if (s.email.isBlank() || !s.email.contains("@")) {
            _state.update { it.copy(error = "El email no es válido") }
            return
        }
        if (s.contrasenaActual.isBlank()) {
            _state.update { it.copy(error = "Ingresa tu contraseña actual para confirmar los cambios") }
            return
        }
        if (s.mostrarCambiarContrasena) {
            if (s.contrasenaNueva.length < 6) {
                _state.update { it.copy(error = "La nueva contraseña debe tener al menos 6 caracteres") }
                return
            }
            if (s.contrasenaNueva != s.confirmarContrasena) {
                _state.update { it.copy(error = "Las contraseñas nuevas no coinciden") }
                return
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Verificar contraseña actual SIN guardar sesión
                val verificacion = authRepository.verificarContrasena(
                    email = usuario.email,
                    contrasena = s.contrasenaActual,
                    tipo = usuario.tipo
                )
                if (verificacion.isFailure) {
                    _state.update { it.copy(isLoading = false, error = "Contraseña actual incorrecta") }
                    return@launch
                }

                val contrasenaFinal = if (s.mostrarCambiarContrasena) s.contrasenaNueva else s.contrasenaActual

                val updateResult = authRepository.updateProfile(
                    id = usuario.id,
                    nombre = s.nombre,
                    email = s.email,
                    contrasena = contrasenaFinal,
                    tipo = usuario.tipo
                )

                if (updateResult.isSuccess) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            updateSuccess = true,
                            mostrarCambiarContrasena = false,
                            contrasenaActual = "",
                            contrasenaNueva = "",
                            confirmarContrasena = ""
                        )
                    }
                    loadUserData()
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = updateResult.exceptionOrNull()?.message ?: "Error al actualizar perfil"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
            }
        }
    }
}
