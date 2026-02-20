package com.carlafdez.autolog.presentation.screens.homeScreen.components

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.Usuario
import com.carlafdez.autolog.presentation.screens.homeScreen.BlueAccent
import com.carlafdez.autolog.presentation.screens.homeScreen.NavyDeep

@Composable
fun HomeHeader(
    usuario: Usuario?,
    vehicleCount: Int?,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(NavyDeep, BlueAccent),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            ).height(120.dp)

    ) {

        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopEnd)
                .graphicsLayer { translationX = 80f; translationY = -60f }
                .background(Color.White.copy(alpha = 0.04f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.BottomStart)
                .graphicsLayer { translationX = -40f; translationY = 40f }
                .background(Color.White.copy(alpha = 0.04f), CircleShape)
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AUTOLOG",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = Color.White.copy(alpha = 0.45f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (usuario != null) "Hola, ${usuario.nombre.split(" ").first()} 👋"
                    else "Bienvenido",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = when (usuario) {
                            is Usuario.MecanicoUsuario -> "Panel de mecánico"
                            is Usuario.ClienteUsuario -> "Panel de cliente"
                            else -> ""
                        },
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Botones de Perfil y Logout
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueAccent.copy(alpha = 0.08f), Color.Transparent)
                )
            )
    )
}