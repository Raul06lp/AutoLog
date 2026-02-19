package com.carlafdez.autolog.presentation.screens.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.presentation.components.AuthTextField
import com.carlafdez.autolog.presentation.components.TipoUsuarioSelector
import com.carlafdez.autolog.ui.theme.Botones
import com.carlafdez.autolog.ui.theme.Fondo
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme
import com.example.autolog.R

private val NavyBlue = Color(0xFF1E3A5F)

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onEvent(LoginEvent.OnLoginSuccessHandled)  // ← resetea el flag
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo AutoLog",
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .width(350.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyBlue),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "INICIAR SESIÓN",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                TipoUsuarioSelector(
                    tipoSeleccionado = state.tipoUsuario,
                    onTipoChange = { onEvent(LoginEvent.OnTipoUsuarioChanged(it)) }
                )

                AuthTextField(
                    value = state.email,
                    onValueChange = { onEvent(LoginEvent.OnEmailChanged(it)) },
                    label = "Correo electrónico",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.fillMaxWidth()
                )

                AuthTextField(
                    value = state.password,
                    onValueChange = { onEvent(LoginEvent.OnPasswordChanged(it)) },
                    label = "Contraseña",
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = Color(0xFFFFCDD2),
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = { onEvent(LoginEvent.OnLoginClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Botones),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("ENTRAR", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "¿No tienes cuenta? Regístrate",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(
        state = LoginUiState(),
        onEvent = {},
        onLoginSuccess = {},
        onNavigateToRegister = {}
    )
}