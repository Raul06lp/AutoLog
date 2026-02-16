package com.carlafdez.pruebakotlin.presentation.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlafdez.pruebakotlin.domain.model.Vehiculo

@Composable
fun CarCard(
    vehicle: Vehiculo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del vehículo
            VehicleImage(
                imagenBase64 = vehicle.imagenBase64,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            // Info del vehículo
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${vehicle.marca} ${vehicle.modelo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = vehicle.matricula,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${vehicle.anio} · ${vehicle.kilometraje} km",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                EstadoBadge(estado = vehicle.estadoRevision)
            }
        }
    }
}

@Composable
private fun VehicleImage(
    imagenBase64: String?,
    modifier: Modifier = Modifier
) {
    if (imagenBase64 != null) {
        val bitmap = remember(imagenBase64) {
            val bytes = Base64.decode(imagenBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
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

    // Placeholder si no hay imagen
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.surfaceVariant,
            RoundedCornerShape(12.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        /*Icon(
            painter = painterResource(id = R.drawable.ic_car_placeholder),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(40.dp)
        )*/
    }
}

@Composable
private fun EstadoBadge(estado: String) {
    val (color, label) = when (estado.lowercase()) {
        "pendiente" -> MaterialTheme.colorScheme.tertiaryContainer to "Pendiente"
        "en_revision", "en revision" -> MaterialTheme.colorScheme.primaryContainer to "En revisión"
        "finalizado" -> MaterialTheme.colorScheme.secondaryContainer to "Finalizado"
        else -> MaterialTheme.colorScheme.surfaceVariant to estado
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// ---- PREVIEWS ----

@Preview(showBackground = true)
@Composable
fun CarCardPreview() {
    MaterialTheme {
        CarCard(
            vehicle = vehiclePreviews.first(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarCardPendientePreview() {
    MaterialTheme {
        CarCard(
            vehicle = vehiclePreviews[1],
            onClick = {}
        )
    }
}

// Datos de prueba con el email del mecánico
val vehiclePreviews = listOf(
    Vehiculo(
        id = 1,
        matricula = "1234ABC",
        marca = "Toyota",
        modelo = "Corolla",
        anio = 2021,
        color = "Rojo",
        kilometraje = 45000,
        observaciones = "Revisión de frenos",
        estadoRevision = "en_revision",
        imagenBase64 = null,
        idCliente = 1,
        nombreCliente = "Carlos López",
        idMecanico = 1,
        nombreMecanico = "mecanico@gmail.com",
        fechaIngreso = "2025-02-09T10:00:00"
    ),
    Vehiculo(
        id = 2,
        matricula = "5678DEF",
        marca = "Honda",
        modelo = "Civic",
        anio = 2019,
        color = "Azul",
        kilometraje = 82000,
        observaciones = "Cambio de aceite",
        estadoRevision = "pendiente",
        imagenBase64 = null,
        idCliente = 2,
        nombreCliente = "María García",
        idMecanico = 1,
        nombreMecanico = "mecanico@gmail.com",
        fechaIngreso = "2025-02-09T11:30:00"
    ),
    Vehiculo(
        id = 3,
        matricula = "9012GHI",
        marca = "Seat",
        modelo = "Ibiza",
        anio = 2022,
        color = "Blanco",
        kilometraje = 12000,
        observaciones = "",
        estadoRevision = "finalizado",
        imagenBase64 = null,
        idCliente = 3,
        nombreCliente = "Pedro Martínez",
        idMecanico = 1,
        nombreMecanico = "mecanico@gmail.com",
        fechaIngreso = "2025-02-08T09:00:00"
    )
)
