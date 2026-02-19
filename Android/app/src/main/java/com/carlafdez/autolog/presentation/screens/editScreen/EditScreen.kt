package com.carlafdez.autolog.presentation.screens.editScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlafdez.autolog.presentation.components.FormField
import com.carlafdez.autolog.presentation.components.SectionTitle
import com.carlafdez.autolog.presentation.screens.editScreen.components.EditImagePicker
import com.carlafdez.autolog.ui.theme.Carta
import com.carlafdez.autolog.ui.theme.Texto

private val NavyBlue = Color(0xFF1E3A5F)
val AccentBlue = Color(0xFF1976D2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    state: EditUiState,
    onEvent: (EditEvent) -> Unit,
    onBack: () -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onEvent(EditEvent.ImagenSeleccionada(uri))
    }

    // Efecto que reacciona al guardado exitoso
    LaunchedEffect(state.guardadoOk) {
        if (state.guardadoOk) {
            onEvent(EditEvent.ResetGuardado) // Consumir el evento inmediatamente
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar vehículo",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Texto,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading && state.matricula.isBlank() -> {
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
                    // --- Selector de imagen ---
                    EditImagePicker(
                        imagenBase64Actual = state.imagenBase64Actual,
                        nuevaImagenUri = state.nuevaImagenUri,
                        onClick = { imagePickerLauncher.launch("image/*") },
                        enabled = !state.isLoading
                    )

                    // --- SECCIÓN: General ---
                    SectionTitle("Información general")

                    FormField(
                        value = state.matricula,
                        onValueChange = { onEvent(EditEvent.MatriculaChanged(it)) },
                        label = "Matrícula",
                        enabled = !state.isLoading
                    )
                    FormField(
                        value = state.marca,
                        onValueChange = { onEvent(EditEvent.MarcaChanged(it)) },
                        label = "Marca",
                        enabled = !state.isLoading
                    )
                    FormField(
                        value = state.modelo,
                        onValueChange = { onEvent(EditEvent.ModeloChanged(it)) },
                        label = "Modelo",
                        enabled = !state.isLoading
                    )

                    // --- SECCIÓN: Especificaciones ---
                    SectionTitle("Especificaciones")

                    FormField(
                        value = state.anio,
                        onValueChange = { onEvent(EditEvent.AnioChanged(it)) },
                        label = "Año",
                        keyboardType = KeyboardType.Number,
                        enabled = !state.isLoading
                    )
                    FormField(
                        value = state.kilometraje,
                        onValueChange = { onEvent(EditEvent.KilometrajeChanged(it)) },
                        label = "Kilómetros",
                        keyboardType = KeyboardType.Number,
                        enabled = !state.isLoading
                    )
                    FormField(
                        value = state.color,
                        onValueChange = { onEvent(EditEvent.ColorChanged(it)) },
                        label = "Color",
                        enabled = !state.isLoading
                    )

                    // --- SECCIÓN: Observaciones ---
                    SectionTitle("Observaciones")

                    OutlinedTextField(
                        value = state.observaciones,
                        onValueChange = { onEvent(EditEvent.ObservacionesChanged(it)) },
                        label = { Text("Observaciones") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5,
                        enabled = !state.isLoading,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Carta,
                            focusedContainerColor = Carta
                        )
                    )

                    // --- SECCIÓN: Medidas tomadas ---
                    SectionTitle("Medidas tomadas")

                    OutlinedTextField(
                        value = state.medidasTomadas,
                        onValueChange = { onEvent(EditEvent.MedidasTomadasChanged(it)) },
                        label = { Text("Medidas tomadas") },
                        placeholder = { Text("Describe las reparaciones realizadas...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 6,
                        enabled = !state.isLoading,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Carta,
                            focusedContainerColor = Carta
                        )
                    )

                    // --- Botón guardar ---
                    Button(
                        onClick = { onEvent(EditEvent.GuardarClick) },
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

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { onEvent(EditEvent.ErrorDismissed) },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { onEvent(EditEvent.ErrorDismissed) }) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview
@Composable
fun EditScreenPreview() {
    EditScreen(
        state = EditUiState(

        ),
        onEvent = {},
        onBack = {}
    )
}