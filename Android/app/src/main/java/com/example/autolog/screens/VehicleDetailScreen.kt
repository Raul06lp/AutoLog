package com.example.autolog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.autolog.core.domain.model.User
import com.example.autolog.feature.vehicle.domain.model.Vehicle
import com.example.autolog.ui.theme.AutoLogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    modifier: Modifier = Modifier,
    vehicle: Vehicle,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalles del Vehículo",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A5F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen del coche
            AsyncImage(
                model = vehicle.imageUrl,
                contentDescription = "Imagen del coche",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // Tarjeta de información general
            InfoCard(title = "Información General") {
                InfoRow(label = "CLIENTE", value = vehicle.cliente.name)
                InfoRow(label = "MARCA", value = vehicle.marca)
                InfoRow(label = "MODELO", value = vehicle.modelo)
                InfoRow(label = "MATRÍCULA", value = vehicle.matricula)
            }

            // Tarjeta de especificaciones
            InfoCard(title = "Especificaciones") {
                InfoRow(label = "AÑO", value = vehicle.year.toString())
                InfoRow(label = "KILÓMETROS", value = vehicle.kilometros)
                InfoRow(label = "COLOR", value = vehicle.color)
            }

            // Tarjeta de observaciones
            if (vehicle.observaciones.isNotEmpty()) {
                InfoCard(title = "Observaciones") {
                    Text(
                        text = vehicle.observaciones,
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            content()
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CarDetailScreenPreview() {
    AutoLogTheme {
        val cocheDemo = Vehicle(
            id = "1",
            imageUrl = "https://media.carsandbids.com/cdn-cgi/image/width=2080,quality=70/438ad923cef6d8239e95d61e7d6849486bae11d9/photos/9lRX14pG-G0xeMwtrjW-(edit).jpg?t=166569778341",
            cliente = User(
                email = "",
                name = "Carla Fernández",
                esMecanico = false,
                vehicles = null
            ),
            marca = "Mazda",
            modelo = "Miata MX5",
            matricula = "1234 ABC",
            year = 1999,
            kilometros = "120.000 km",
            color = "Rojo",
            observaciones = "Vehículo en excelente estado. Requiere cambio de aceite y revisión de frenos. El propietario menciona un ruido extraño al acelerar en segunda marcha."
        )

        CarDetailScreen(vehicle = cocheDemo)
    }
}