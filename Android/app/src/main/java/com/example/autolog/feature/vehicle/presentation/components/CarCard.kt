package com.example.autolog.feature.vehicle.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CarCard(
    modifier: Modifier = Modifier,
    imagen: Uri?,
    cliente: String,
    marca: String,
    modelo: String,
    onDetailsClick: () -> Unit,
    onCompleteClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .width(200.dp),
        shape = RoundedCornerShape(24.dp),
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del coche
            AsyncImage(
                model = imagen,
                contentDescription = "Imagen del coche",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Información del cliente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "CLIENTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = cliente,
                    fontSize = 10.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Información de la marca
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "MARCA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = marca,
                    fontSize = 10.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Información del modelo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "MODELO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = modelo,
                    fontSize = 10.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (onCompleteClick != null)
                    Arrangement.SpaceBetween
                else
                    Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de añadir/detalles (solo si hay botón de completar)
                if (onCompleteClick != null) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onDetailsClick) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Ver detalles",
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Botón de completar o detalles (según el caso)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (onCompleteClick != null)
                                Color(0xFFE8F5E9)
                            else
                                Color(0xFFE3F2FD)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onCompleteClick ?: onDetailsClick
                    ) {
                        Icon(
                            imageVector = if (onCompleteClick != null)
                                Icons.Default.Check
                            else
                                Icons.Default.Add,
                            contentDescription = if (onCompleteClick != null)
                                "Marcar como completado"
                            else
                                "Ver detalles",
                            tint = if (onCompleteClick != null)
                                Color(0xFF4CAF50)
                            else
                                Color(0xFF1976D2),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vista del mecánico (con dos botones)
        Text("Vista del mecánico:", fontWeight = FontWeight.Bold)
        CarCard(
            imagen = null,
            cliente = "Carla Fernandez",
            marca = "Mazda",
            modelo = "Miata mx5",
            onDetailsClick = { },
            onCompleteClick = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Vista del cliente (solo un botón)
        Text("Vista del cliente:", fontWeight = FontWeight.Bold)
        CarCard(
            imagen = null,
            cliente = "Carla Fernandez",
            marca = "Mazda",
            modelo = "Miata mx5",
            onDetailsClick = { }
        )
    }
}