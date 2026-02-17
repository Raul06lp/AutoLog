package com.carlafdez.autolog.presentation.screens.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.ui.theme.Botones
import com.carlafdez.autolog.ui.theme.Fondo
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido a",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "AutoLog",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Botones,
            fontSize = 40.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(LoginEvent.OnEmailChanged(it)) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.OnPasswordChanged(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { onEvent(LoginEvent.OnLoginClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Botones),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    PruebaKotlinTheme {
        LoginScreen(
            state = LoginUiState(),
            onEvent = {},
            onLoginSuccess = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginLoadingPreview() {
    PruebaKotlinTheme {
        LoginScreen(
            state = LoginUiState(isLoading = true),
            onEvent = {},
            onLoginSuccess = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginErrorPreview() {
    PruebaKotlinTheme {
        LoginScreen(
            state = LoginUiState(error = "Credenciales incorrectas"),
            onEvent = {},
            onLoginSuccess = {}
        )
    }
}