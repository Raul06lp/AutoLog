package com.carlafdez.autolog.presentation.screens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlafdez.autolog.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> _state.update { it.copy(email = event.email, error = null) }
            is LoginEvent.OnPasswordChanged -> _state.update { it.copy(password = event.password, error = null) }
            is LoginEvent.OnTipoUsuarioChanged -> _state.update { it.copy(tipoUsuario = event.tipo, error = null) }
            LoginEvent.OnLoginClick -> login()
            LoginEvent.OnLoginSuccessHandled ->  _state.update { it.copy(isLoginSuccessful = false) }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.login(
                email = _state.value.email.trim(),
                contrasena = _state.value.password,
                tipo = _state.value.tipoUsuario
            ).onSuccess {
                _state.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            }.onFailure {
                _state.update { it.copy(isLoading = false, error = "Credenciales incorrectas") }
            }
        }
    }
}
