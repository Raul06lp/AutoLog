package com.example.autolog.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autolog.ui.theme.AutoLogTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onClick: (String, String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(.5f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("Prueba") }

        Text("Login")

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(

                )
            }
        )

        Button(onClick = { onClick(username, password) }) {
            Text(text = "Entrar")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    AutoLogTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LoginScreen(
                modifier = Modifier.padding(innerPadding),
                onClick = { _, _ -> }
            )
        }
    }
}