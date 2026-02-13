package com.example.autolog.feature.vehicle.presentation.edit_vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.autolog.feature.vehicle.domain.model.Vehicle
import com.example.autolog.ui.theme.AutoLogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarEditScreen(
    modifier: Modifier = Modifier,
    vehicle: Vehicle,
    onBackClick: () -> Unit = {},
    onSaveClick: (Vehicle) -> Unit = {}
) {
    var cliente by remember { mutableStateOf(vehicle.cliente.name) }
    var marca by remember { mutableStateOf(vehicle.marca) }
    var modelo by remember { mutableStateOf(vehicle.modelo) }
    var matricula by remember { mutableStateOf(vehicle.matricula) }
    var year by remember { mutableStateOf(vehicle.year.toString()) }
    var kilometros by remember { mutableStateOf(vehicle.kilometros) }
    var color by remember { mutableStateOf(vehicle.color) }
    var observaciones by remember { mutableStateOf(vehicle.observaciones) }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Vehículo",
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
                model = vehicle.imagen,
                contentDescription = "Imagen del coche",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // Campos de información general
            Text(
                text = "Información General",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = matricula,
                onValueChange = { matricula = it },
                label = { Text("Matrícula") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campos de especificaciones
            Text(
                text = "Especificaciones",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Año") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = kilometros,
                onValueChange = { kilometros = it },
                label = { Text("Kilómetros") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Campo de observaciones
            Text(
                text = "Observaciones",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            // Campo de medidas tomadas
            Text(
                text = "Medidas tomadas",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            // Botón de guardar
            Button(
                onClick = {
                    onSaveClick
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Guardar Cambios",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CarEditScreenPreview() {
    AutoLogTheme {
        val cocheDemo = Vehicle(
            id = "1",
            imagen = null,
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
            observaciones = "Vehículo en excelente estado. Requiere cambio de aceite y revisión de frenos.",
        )

        CarEditScreen(vehicle = cocheDemo)
    }
}