package com.carlafdez.autolog.presentation.screens.addScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.carlafdez.autolog.domain.model.Cliente

private val NavyBlue = Color(0xFF1E3A5F)
private val AccentBlue = Color(0xFF1976D2)

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
                        text = "Añadir Vehículo",
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
                enabled = !state.isLoading
            )

            // --- Botón guardar ---
            Button(
                onClick = { onEvent(AddEvent.GuardarClick) },
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
                    Text("Añadir Vehículo", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showClienteSheet) {
        ClienteBottomSheet(
            clientes = state.clientes,
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

@Composable
private fun ImagePicker(
    imagenUri: Uri?,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .border(2.dp, AccentBlue, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imagenUri != null) {
            AsyncImage(
                model = imagenUri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = AccentBlue
                )
                Text(
                    text = "Toca para añadir foto",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClienteBottomSheet(
    clientes: List<Cliente>,
    clienteSeleccionado: Cliente?,
    onClienteClick: (Cliente) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Seleccionar cliente",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = NavyBlue,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()

            if (clientes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentBlue)
                }
            } else {
                clientes.forEach { cliente ->
                    val seleccionado = cliente.id == clienteSeleccionado?.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClienteClick(cliente) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = cliente.nombre,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                color = if (seleccionado) AccentBlue else Color.Black
                            )
                            Text(text = cliente.email, fontSize = 12.sp, color = Color.Gray)
                        }
                        if (seleccionado) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = AccentBlue)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ClienteSelector(
    clienteSeleccionado: Cliente?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = clienteSeleccionado?.nombre ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Text("Cliente") },
        placeholder = { Text(if (isLoading) "Cargando clientes..." else "Selecciona un cliente") },
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = Color.Gray,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, color = AccentBlue, fontSize = 13.sp)
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        enabled = enabled
    )
}

// ---- PREVIEWS ----

@Preview(showSystemUi = true)
@Composable
fun AddScreenPreview() {
    MaterialTheme {
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
    MaterialTheme {
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
