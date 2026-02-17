package com.carlafdez.autolog.presentation.screens.detailScreen

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.Vehiculo
import com.carlafdez.autolog.presentation.components.vehiclePreviews
import com.carlafdez.autolog.ui.theme.Botones
import com.carlafdez.autolog.ui.theme.Carta
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme
import com.carlafdez.autolog.ui.theme.Texto

private val NavyBlue = Color(0xFF1E3A5F)
private val AccentBlue = Color(0xFF1976D2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    state: VehicleDetailUiState,
    onEvent: (VehicleDetailEvent) -> Unit,
    onBack: () -> Unit,
    onEditClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.vehiculo?.let { "${it.marca} ${it.modelo}" } ?: "Detalle",
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
                colors = TopAppBarDefaults.topAppBarColors(Texto)
            )
        },
        floatingActionButton = {
            if (state.vehiculo != null) {
                FloatingActionButton(
                    onClick = onEditClick,
                    containerColor = Botones,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
            }
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentBlue)
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(state.error, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onEvent(VehicleDetailEvent.Retry) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            state.vehiculo != null -> {
                VehicleDetailContent(
                    vehiculo = state.vehiculo,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun VehicleDetailContent(
    vehiculo: Vehiculo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen arriba del todo
        VehicleHeaderImage(
            imagenBase64 = vehiculo.imagenBase64,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${vehiculo.marca} ${vehiculo.modelo}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue
                    )
                    Text(
                        text = vehiculo.matricula,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                EstadoChip(estado = vehiculo.estadoRevision)
            }

            HorizontalDivider()

            // Sección: Información general
            DetailSection(title = "Información general") {
                DetailRow(label = "Año", value = vehiculo.anio.toString())
                DetailRow(label = "Color", value = vehiculo.color.ifBlank { "—" })
                DetailRow(label = "Kilometraje", value = "${vehiculo.kilometraje} km")
                DetailRow(label = "Fecha de ingreso", value = vehiculo.fechaIngreso.take(10))
            }

            HorizontalDivider()

            // Sección: Cliente
            DetailSection(title = "Cliente") {
                DetailRow(label = "Nombre", value = vehiculo.nombreCliente)
            }

            HorizontalDivider()

            // Sección: Observaciones
            if (vehiculo.observaciones.isNotBlank()) {
                DetailSection(title = "Observaciones") {
                    Text(
                        text = vehiculo.observaciones,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                }
                HorizontalDivider()
            }

            // Sección: Medidas
            if (vehiculo.medidasTomadas.isNotBlank()) {
                DetailSection(title = "Medidas tomadas") {
                    Text(
                        text = vehiculo.medidasTomadas,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                }
                HorizontalDivider()
            }

            // Espacio para que el FAB no tape el contenido
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun VehicleHeaderImage(imagenBase64: String?, modifier: Modifier = Modifier) {
    if (imagenBase64 != null) {
        val bitmap = remember(imagenBase64) {
            runCatching {
                val bytes = Base64.decode(imagenBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
            }.getOrNull()
        }
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Foto del vehículo",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
            return
        }
    }

    // Placeholder
    Box(
        modifier = modifier.background(Carta),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = null,
            tint = AccentBlue,
            modifier = Modifier.size(96.dp)
        )
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Texto,
            letterSpacing = 0.5.sp
        )
        content()
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun EstadoChip(estado: String) {
    val (bgColor, label) = when (estado.lowercase()) {
        "pendiente" -> Color(0xFFFFF3E0) to "Pendiente"
        "en_revision", "en revision" -> Color(0xFFE3F2FD) to "En revisión"
        "finalizado" -> Color(0xFFE8F5E9) to "Finalizado"
        else -> Color(0xFFF5F5F5) to estado
    }
    val textColor = when (estado.lowercase()) {
        "pendiente" -> Color(0xFFE65100)
        "en_revision", "en revision" -> AccentBlue
        "finalizado" -> Color(0xFF2E7D32)
        else -> Color.Gray
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
        )
    }
}

// ---- PREVIEWS ----

@Preview(showSystemUi = true)
@Composable
fun VehicleDetailScreenPreview() {
    PruebaKotlinTheme {
        VehicleDetailScreen(
            state = VehicleDetailUiState(vehiculo = vehiclePreviews[0]),
            onEvent = {},
            onBack = {},
            onEditClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun VehicleDetailLoadingPreview() {
    PruebaKotlinTheme {
        VehicleDetailScreen(
            state = VehicleDetailUiState(isLoading = true),
            onEvent = {},
            onBack = {},
            onEditClick = {}
        )
    }
}
