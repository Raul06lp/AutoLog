package com.carlafdez.autolog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.TipoUsuario

@Composable
fun TipoUsuarioSelector(
    tipoSeleccionado: TipoUsuario,
    onTipoChange: (TipoUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = Color(0xFF1976D2)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Botón Mecánico
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (tipoSeleccionado == TipoUsuario.MECANICO) accentColor
                    else Color.Transparent
                )
                .clickable { onTipoChange(TipoUsuario.MECANICO) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MECÁNICO",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = if (tipoSeleccionado == TipoUsuario.MECANICO)
                    FontWeight.Bold else FontWeight.Normal
            )
        }

        // Botón Cliente
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (tipoSeleccionado == TipoUsuario.CLIENTE) accentColor
                    else Color.Transparent
                )
                .clickable { onTipoChange(TipoUsuario.CLIENTE) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "CLIENTE",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = if (tipoSeleccionado == TipoUsuario.CLIENTE)
                    FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
