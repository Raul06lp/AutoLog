package com.carlafdez.autolog.presentation.screens.editScreen

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val NavyBlue = Color(0xFF1E3A5F)
private val AccentBlue = Color(0xFF1976D2)

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

    LaunchedEffect(state.guardadoOk) {
        if (state.guardadoOk) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Vehículo",
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
                        enabled = !state.isLoading
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
                        enabled = !state.isLoading
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

@Composable
private fun EditImagePicker(
    imagenBase64Actual: String?,
    nuevaImagenUri: Uri?,
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
        when {
            // Prioridad 1: imagen nueva seleccionada por el usuario
            nuevaImagenUri != null -> {
                AsyncImage(
                    model = nuevaImagenUri,
                    contentDescription = "Nueva imagen",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Badge "Nueva foto"
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = AccentBlue,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Nueva foto",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Prioridad 2: imagen existente en el servidor
            imagenBase64Actual != null -> {
                val bitmap = remember(imagenBase64Actual) {
                    runCatching {
                        val bytes = Base64.decode(imagenBase64Actual, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                    }.getOrNull()
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Imagen actual",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                // Badge "Toca para cambiar"
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Toca para cambiar la foto",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Sin imagen
            else -> {
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
fun EditScreenPreview() {
    MaterialTheme {
        EditScreen(
            state = EditUiState(
                matricula = "1234ABC",
                marca = "Toyota",
                modelo = "Corolla",
                anio = "2021",
                color = "Rojo",
                kilometraje = "45000",
                observaciones = "Revisión de frenos"
            ),
            onEvent = {},
            onBack = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun EditScreenLoadingPreview() {
    MaterialTheme {
        EditScreen(
            state = EditUiState(isLoading = true),
            onEvent = {},
            onBack = {}
        )
    }
}
