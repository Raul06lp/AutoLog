package com.carlafdez.autolog.presentation.screens.detailScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.Vehiculo
import com.carlafdez.autolog.presentation.screens.detailScreen.NavyBlue

@Composable
fun VehicleDetailContent(
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            DetailSection(title = "Información general") {
                DetailRow(label = "Año", value = vehiculo.anio.toString())
                DetailRow(label = "Color", value = vehiculo.color.ifBlank { "—" })
                DetailRow(label = "Kilometraje", value = "${vehiculo.kilometraje} km")
                DetailRow(label = "Fecha de ingreso", value = vehiculo.fechaIngreso.take(10))
            }

            HorizontalDivider()

            DetailSection(title = "Cliente") {
                DetailRow(label = "Nombre", value = vehiculo.nombreCliente)
            }

            HorizontalDivider()

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

            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}