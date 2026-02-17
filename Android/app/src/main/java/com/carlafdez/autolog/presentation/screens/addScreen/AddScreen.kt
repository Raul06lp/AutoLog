package com.carlafdez.autolog.presentation.screens.addScreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlafdez.autolog.domain.model.Cliente
import com.carlafdez.autolog.presentation.screens.addScreen.components.ClienteBottomSheet
import com.carlafdez.autolog.presentation.screens.addScreen.components.ClienteSelector
import com.carlafdez.autolog.presentation.components.FormField
import com.carlafdez.autolog.presentation.screens.addScreen.components.ImagePicker
import com.carlafdez.autolog.presentation.components.SectionTitle
import com.carlafdez.autolog.ui.theme.Carta
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme
import com.carlafdez.autolog.ui.theme.Texto

private val NavyBlue = Color(0xFF1E3A5F)
val AccentBlue = Color(0xFF1976D2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    state: AddUiState,
    onEvent: (AddEvent) -> Unit,
    onBack: () -> Unit
) {
    var showClienteSheet by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onEvent(AddEvent.ImagenSeleccionada(uri))
    }

    LaunchedEffect(state.guardadoOk) {
        if (state.guardadoOk) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Añadir vehículo",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Texto)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Selector de imagen ---
            ImagePicker(
                imagenUri = state.imagenUri,
                onClick = { imagePickerLauncher.launch("image/*") },
                enabled = !state.isLoading
            )

            // --- SECCIÓN: General ---
            SectionTitle("Información general")

            ClienteSelector(
                clienteSeleccionado = state.clienteSeleccionado,
                isLoading = state.isLoadingClientes,
                onClick = { showClienteSheet = true }
            )
            FormField(
                value = state.matricula,
                onValueChange = { onEvent(AddEvent.MatriculaChanged(it)) },
                label = "Matrícula",
                enabled = !state.isLoading
            )
            FormField(
                value = state.marca,
                onValueChange = { onEvent(AddEvent.MarcaChanged(it)) },
                label = "Marca",
                enabled = !state.isLoading
            )
            FormField(
                value = state.modelo,
                onValueChange = { onEvent(AddEvent.ModeloChanged(it)) },
                label = "Modelo",
                enabled = !state.isLoading
            )

            // --- SECCIÓN: Especificaciones ---
            SectionTitle("Especificaciones")

            FormField(
                value = state.anio,
                onValueChange = { onEvent(AddEvent.AnioChanged(it)) },
                label = "Año",
                keyboardType = KeyboardType.Number,
                enabled = !state.isLoading
            )
            FormField(
                value = state.kilometraje,
                onValueChange = { onEvent(AddEvent.KilometrajeChanged(it)) },
                label = "Kilómetros",
                keyboardType = KeyboardType.Number,
                enabled = !state.isLoading
            )
            FormField(
                value = state.color,
                onValueChange = { onEvent(AddEvent.ColorChanged(it)) },
                label = "Color",
                enabled = !state.isLoading
            )

            // --- SECCIÓN: Observaciones ---
            SectionTitle("Observaciones")

            OutlinedTextField(
                value = state.observaciones,
                onValueChange = { onEvent(AddEvent.ObservacionesChanged(it)) },
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

            // --- Botón guardar ---
            Button(
                onClick = { onEvent(AddEvent.GuardarClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Texto),
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
                    Text("Añadir vehículo", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showClienteSheet) {
        ClienteBottomSheet(
            clientes = state.clientes,
            isLoading = state.isLoadingClientes,
            clienteSeleccionado = state.clienteSeleccionado,
            onClienteClick = {
                onEvent(AddEvent.ClienteSeleccionado(it))
                showClienteSheet = false
            },
            onDismiss = { showClienteSheet = false }
        )
    }

    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { onEvent(AddEvent.ErrorDismissed) },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { onEvent(AddEvent.ErrorDismissed) }) {
                    Text("OK")
                }
            }
        )
    }
}

// ---- PREVIEWS ----

@Preview(showSystemUi = true)
@Composable
fun AddScreenPreview() {
    PruebaKotlinTheme {
        AddScreen(
            state = AddUiState(
                clientes = listOf(
                    Cliente(1, "Carlos López", "carlos@email.com"),
                    Cliente(2, "María García", "maria@email.com")
                )
            ),
            onEvent = {},
            onBack = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddScreenConClientePreview() {
    PruebaKotlinTheme {
        AddScreen(
            state = AddUiState(
                clienteSeleccionado = Cliente(1, "Carlos López", "carlos@email.com"),
                marca = "Toyota",
                modelo = "Corolla",
                matricula = "1234ABC",
                anio = "2021"
            ),
            onEvent = {},
            onBack = {}
        )
    }
}
