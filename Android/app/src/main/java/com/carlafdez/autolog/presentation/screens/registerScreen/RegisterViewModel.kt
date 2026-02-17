package com.carlafdez.autolog.presentation.screens.registerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnNombreChanged -> _state.update { it.copy(nombre = event.value, error = null) }
            is RegisterEvent.OnEmailChanged -> _state.update { it.copy(email = event.value, error = null) }
            is RegisterEvent.OnPasswordChanged -> _state.update { it.copy(password = event.value, error = null) }
            is RegisterEvent.OnConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.value, error = null) }
            RegisterEvent.OnRegisterClick -> register()
        }
    }

    private fun register() {
        val s = _state.value
        when {
            s.nombre.isBlank() || s.email.isBlank() || s.password.isBlank() ->
                _state.update { it.copy(error = "Por favor, completa todos los campos") }
            !s.email.contains("@") ->
                _state.update { it.copy(error = "El email no es válido") }
            s.password.length < 6 ->
                _state.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres") }
            s.password != s.confirmPassword ->
                _state.update { it.copy(error = "Las contraseñas no coinciden") }
            else -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true, error = null) }
                    authRepository.register(
                        nombre = s.nombre.trim(),
                        email = s.email.trim(),
                        contrasena = s.password
                    ).onSuccess {
                        _state.update { it.copy(isLoading = false, isRegisterSuccessful = true) }
                    }.onFailure {
                        _state.update { it.copy(isLoading = false, error = "Este email ya está registrado") }
                    }
                }
            }
        }
    }
}
