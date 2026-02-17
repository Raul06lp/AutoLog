package com.carlafdez.autolog.presentation.screens.detailScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlafdez.autolog.presentation.screens.detailScreen.components.VehicleDetailContent
import com.carlafdez.autolog.presentation.screens.homeScreen.components.vehiclePreviews
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.Vehiculo
import com.carlafdez.autolog.presentation.components.vehiclePreviews
import com.carlafdez.autolog.presentation.screens.detailScreen.components.DetailRow
import com.carlafdez.autolog.presentation.screens.detailScreen.components.DetailSection
import com.carlafdez.autolog.presentation.screens.detailScreen.components.EstadoChip
import com.carlafdez.autolog.presentation.screens.detailScreen.components.VehicleHeaderImage
>>>>>>> Stashed changes
import com.carlafdez.autolog.ui.theme.Botones
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme
import com.carlafdez.autolog.ui.theme.Texto

val NavyBlue = Color(0xFF1E3A5F)
private val NavyBlue = Color(0xFF1E3A5F)
val AccentBlue = Color(0xFF1976D2)

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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de cambiar estado (solo si no está finalizado)
                    if (state.vehiculo.estadoRevision != "finalizado") {
                        val (textoBoton, iconoBoton, colorBoton) = when (state.vehiculo.estadoRevision) {
                            "pendiente" -> Triple("Iniciar Revisión", Icons.Default.Build, Color(0xFFFF9800))
                            "en_revision" -> Triple("Finalizar", Icons.Default.CheckCircle, Color(0xFF4CAF50))
                            else -> Triple("Iniciar Revisión", Icons.Default.Build, Color(0xFFFF9800))
                        }

                        ExtendedFloatingActionButton(
                            onClick = { onEvent(VehicleDetailEvent.CambiarEstado) },
                            containerColor = colorBoton,
                            contentColor = Color.White,
                            icon = { Icon(iconoBoton, contentDescription = null) },
                            text = { Text(textoBoton) }
                        )
                    }

                    // Botón de editar
                    FloatingActionButton(
                        onClick = onEditClick,
                        containerColor = Botones,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
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
