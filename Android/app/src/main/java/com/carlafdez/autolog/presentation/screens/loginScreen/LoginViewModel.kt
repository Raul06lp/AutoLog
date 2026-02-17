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
            is LoginEvent.OnEmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }
            is LoginEvent.OnPasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            LoginEvent.OnLoginClick -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val result = authRepository.login(
                email = _state.value.email,
                contrasena = _state.value.password
            )

            result.onSuccess {
                _state.update { it.copy(
                    isLoading = false,
                    isLoginSuccessful = true
                ) }
            }.onFailure { e ->
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                ) }
            }
        }
    }
}