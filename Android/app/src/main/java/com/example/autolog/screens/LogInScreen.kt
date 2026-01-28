package com.example.autolog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autolog.cars.presentation.components.AuthTextField
import com.example.autolog.ui.theme.AutoLogTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit = {}
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
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
            ) {
                var username by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                Text(
                    text = "INICIAR SESIÓN",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                AuthTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Usuario",
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Next
                )

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true,
                    imeAction = ImeAction.Done
                )

                Button(
                    onClick = { onLoginClick(username, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ENTRAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth()
                ) {
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

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    AutoLogTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LoginScreen(
                modifier = Modifier.padding(innerPadding),
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }
    }
}