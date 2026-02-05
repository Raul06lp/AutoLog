package com.example.autolog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autolog.cars.presentation.components.AuthTextField
import com.example.autolog.ui.theme.AutoLogTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: (String, String, String, Boolean) -> Unit,
    onNavigateToLogin: () -> Unit = {}
) {
    val mechanicBlue = Color(0xFF1E3A5F)
    val lightBlue = Color(0xFFCAF0F8)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(lightBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = com.example.autolog.R.drawable.logo_app
            ),
            contentDescription = "Logo"
        )
        
        Card(
            modifier = Modifier
                .width(350.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = mechanicBlue
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
            ) {
                var name by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var confirmPassword by remember { mutableStateOf("") }
                var esMecanico by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf("") }

                Text(
                    text = "CREAR CUENTA",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                AuthTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        errorMessage = ""
                    },
                    label = "Nombre completo",
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Next
                )

                AuthTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        errorMessage = ""
                    },
                    label = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )

                AuthTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        errorMessage = ""
                    },
                    label = "Contraseña",
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true,
                    imeAction = ImeAction.Next
                )

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it
                        errorMessage = ""
                    },
                    label = "Confirmar contraseña",
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true,
                    imeAction = ImeAction.Done
                )

                // Checkbox para mecánico
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(
                        checked = esMecanico,
                        onCheckedChange = { esMecanico = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White.copy(alpha = 0.7f),
                            checkmarkColor = mechanicBlue
                        )
                    )
                    Text(
                        text = "Registrarme como mecánico",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                // Mensaje de error
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFFFCDD2),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = {
                        when {
                            name.isEmpty() || email.isEmpty() || 
                            password.isEmpty() || confirmPassword.isEmpty() -> {
                                errorMessage = "Por favor, completa todos los campos"
                            }
                            !email.contains("@") -> {
                                errorMessage = "Por favor, introduce un email válido"
                            }
                            password.length < 6 -> {
                                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                            }
                            password != confirmPassword -> {
                                errorMessage = "Las contraseñas no coinciden"
                            }
                            else -> {
                                onRegisterClick(name, email, password, esMecanico)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "REGISTRARSE", 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Ya tienes cuenta? Inicia sesión",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    AutoLogTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RegisterScreen(
                modifier = Modifier.padding(innerPadding),
                onRegisterClick = { _, _, _, _ -> },
                onNavigateToLogin = {}
            )
        }
    }
}
