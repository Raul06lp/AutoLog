package com.carlafdez.autolog.presentation.screens.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.presentation.screens.homeScreen.BlueAccent
import com.carlafdez.autolog.presentation.screens.homeScreen.NavyDeep

@Composable
fun EmptyState(
    isMecanico: Boolean,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(BlueAccent.copy(alpha = 0.12f), Color.Transparent)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = BlueAccent.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (isMecanico) "Sin vehículos aún" else "Sin vehículos asignados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDeep
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isMecanico)
                "Añade el primer vehículo para empezar"
            else
                "Tu mecánico registrará tu vehículo y aparecerá aquí",
            fontSize = 13.sp,
            color = Color(0xFF8FA3CC),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        if (isMecanico) {
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = BlueAccent.copy(alpha = 0.3f),
                    spotColor = BlueAccent.copy(alpha = 0.3f)
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Añadir vehículo", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}