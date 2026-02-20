package com.carlafdez.autolog.presentation.screens.detailScreen.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlafdez.autolog.presentation.screens.detailScreen.AccentBlue
import com.carlafdez.autolog.ui.theme.Carta

@Composable
fun VehicleHeaderImage(
    imagenBase64: String?,
    modifier: Modifier = Modifier
) {
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
        } else {
            DefaultPlaceholder(modifier)
        }
    } else {
        DefaultPlaceholder(modifier)
    }
}

@Composable
private fun DefaultPlaceholder(modifier: Modifier) {
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
