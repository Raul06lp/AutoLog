package com.carlafdez.autolog.presentation.screens.detailScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.presentation.screens.detailScreen.AccentBlue

@Composable
fun EstadoChip(estado: String) {
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