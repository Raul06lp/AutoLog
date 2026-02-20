package com.carlafdez.autolog.presentation.screens.profileScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.TipoUsuario
import com.carlafdez.autolog.presentation.components.FormField
import com.carlafdez.autolog.presentation.components.SectionTitle

private val NavyBlue = Color(0xFF1E3A5F)
private val AccentBlue = Color(0xFF1976D2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    onBack: () -> Unit
) {
    // Mostrar mensaje de éxito
    LaunchedEffect(state.updateSuccess) {
        if (state.updateSuccess) {
            kotlinx.coroutines.delay(1500)
            onEvent(ProfileEvent.SuccessDismissed)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyBlue)
            )
        }
    ) { padding ->
        when {
            state.isLoading && state.nombre.isBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentBlue)
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Información básica
                    SectionTitle("Información personal")

                    FormField(
                        value = state.nombre,
                        onValueChange = { onEvent(ProfileEvent.NombreChanged(it)) },
                        label = "Nombre completo",
                        enabled = !state.isLoading
                    )

                    FormField(
                        value = state.email,
                        onValueChange = { onEvent(ProfileEvent.EmailChanged(it)) },
                        label = "Email",
                        keyboardType = KeyboardType.Email,
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Toggle para cambiar contraseña
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onEvent(ProfileEvent.ToggleCambiarContrasena) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Cambiar contraseña",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (state.mostrarCambiarContrasena) "Ocultar campos" else "Mostrar campos",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Switch(
                                checked = state.mostrarCambiarContrasena,
                                onCheckedChange = { onEvent(ProfileEvent.ToggleCambiarContrasena) }
                            )
                        }
                    }

                    // Campos de contraseña (si está activado el toggle)
                    if (state.mostrarCambiarContrasena) {
                        SectionTitle("Cambiar contraseña")

                        FormField(
                            value = state.contrasenaActual,
                            onValueChange = { onEvent(ProfileEvent.ContrasenaActualChanged(it)) },
                            label = "Contraseña actual",
                            isPassword = true,
                            enabled = !state.isLoading
                        )

                        FormField(
                            value = state.contrasenaNueva,
                            onValueChange = { onEvent(ProfileEvent.ContrasenaNuevaChanged(it)) },
                            label = "Nueva contraseña",
                            isPassword = true,
                            enabled = !state.isLoading
                        )

                        FormField(
                            value = state.confirmarContrasena,
                            onValueChange = { onEvent(ProfileEvent.ConfirmarContrasenaChanged(it)) },
                            label = "Confirmar contraseña",
                            isPassword = true,
                            enabled = !state.isLoading,
                            imeAction = ImeAction.Done
                        )
                    } else {
                        // Si no está cambiando contraseña, pedir la actual para confirmar cambios
                        SectionTitle("Confirmar identidad")
                        
                        FormField(
                            value = state.contrasenaActual,
                            onValueChange = { onEvent(ProfileEvent.ContrasenaActualChanged(it)) },
                            label = "Contraseña actual",
                            isPassword = true,
                            enabled = !state.isLoading,
                            imeAction = ImeAction.Done
                        )
                        
                        Text(
                            text = "Ingresa tu contraseña para confirmar los cambios",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón guardar
                    Button(
                        onClick = { onEvent(ProfileEvent.GuardarCambios) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Guardar cambios", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Diálogo de error
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { onEvent(ProfileEvent.ErrorDismissed) },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { onEvent(ProfileEvent.ErrorDismissed) }) {
                    Text("OK")
                }
            }
        )
    }

    // Snackbar de éxito
    if (state.updateSuccess) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    text = "✓ Perfil actualizado correctamente",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
