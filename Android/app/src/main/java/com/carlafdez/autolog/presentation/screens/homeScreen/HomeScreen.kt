package com.carlafdez.autolog.presentation.screens.homeScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.Usuario
import com.carlafdez.autolog.presentation.screens.homeScreen.components.CarCard
import com.carlafdez.autolog.presentation.screens.homeScreen.components.vehiclePreviews
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme
import kotlinx.coroutines.delay


private val NavyDeep   = Color(0xFF0F1E4E)
private val BlueAccent = Color(0xFF2563EB)
private val BgLight    = Color(0xFFF4F6FB)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onAddClick: () -> Unit
) {
    val isMecanico = state.usuario is Usuario.MecanicoUsuario

    Scaffold(
        containerColor = BgLight,
        floatingActionButton = {
            if (isMecanico) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = BlueAccent,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = BlueAccent.copy(alpha = 0.35f),
                        spotColor = BlueAccent.copy(alpha = 0.35f)
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Añadir vehículo",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading && state.vehicles.isNotEmpty(),
            onRefresh = { onEvent(HomeEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    HomeHeroHeader(
                        usuario = state.usuario,
                        vehicleCount = state.vehicles.size.takeIf { !state.isLoading },
                        onLogoutClick = { onEvent(HomeEvent.Logout) }
                    )
                }
                when {
                    state.isLoading && state.vehicles.isEmpty() -> {
                        items(5) {
                            SkeletonCard(modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp))
                        }
                    }

                    state.error != null && state.vehicles.isEmpty() -> {
                        item {
                            ErrorState(
                                message = state.error,
                                onRetry = { onEvent(HomeEvent.Refresh) }
                            )
                        }
                    }

                    state.vehicles.isEmpty() -> {
                        item {
                            EmptyState(
                                isMecanico = isMecanico,
                                onAddClick = onAddClick
                            )
                        }
                    }

                    else -> {
                        itemsIndexed(
                            items = state.vehicles,
                            key = { _, v -> v.id }
                        ) { index, vehicle ->
                            AnimatedItem(index = index) {
                                CarCard(
                                    vehicle = vehicle,
                                    onClick = { onEvent(HomeEvent.VehicleClicked(vehicle.id)) },
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeroHeader(
    usuario: Usuario?,
    vehicleCount: Int?,
    onLogoutClick: () -> Unit = {}
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
            )
            .height(120.dp)
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
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .fillMaxWidth(),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = when (usuario) {
                            is Usuario.MecanicoUsuario -> "Panel de mecánico"
                            else -> "Panel de cliente"
                        },
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        fontWeight = FontWeight.Medium
                    )
                    vehicleCount?.takeIf { it > 0 }?.let { count ->
                        Surface(
                            shape = RoundedCornerShape(99.dp),
                            color = Color.White.copy(alpha = 0.14f)
                        ) {
                            Text(
                                text = "$count vehículo${if (count != 1) "s" else ""}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White.copy(alpha = 0.80f),
                    modifier = Modifier.size(20.dp)
                )
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


@Composable
private fun AnimatedItem(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((index * 60L).coerceAtMost(300L))
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "item_alpha"
    )
    val translationY by animateFloatAsState(
        targetValue = if (visible) 0f else 24f,
        animationSpec = tween(durationMillis = 350),
        label = "item_translation"
    )

    Box(
        modifier = Modifier
            .alpha(alpha)
            .graphicsLayer { this.translationY = translationY }
    ) {
        content()
    }
}

@Composable
private fun EmptyState(
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


@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
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
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Algo ha ido mal",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDeep
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            fontSize = 13.sp,
            color = Color(0xFF8FA3CC),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Text("Reintentar", fontWeight = FontWeight.SemiBold)
        }
    }
}


@Composable
private fun SkeletonCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(15.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE8EDF5),
                Color(0xFFF8FAFF),
                Color(0xFFE8EDF5)
            ),
            start = Offset(translateAnim - 300f, 0f),
            end = Offset(translateAnim, 0f)
        )
    )
}


@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PruebaKotlinTheme {
        HomeScreen(
            state = HomeUiState(
                usuario = Usuario.MecanicoUsuario(id = 1, nombre = "Carlos López", email = "carlos@email.com"),
                vehicles = vehiclePreviews,
                isLoading = false,
                error = null
            ),
            onEvent = {},
            onAddClick = {}
        )
    }
}

@Preview(showSystemUi = true, name = "Empty — Mecánico")
@Composable
fun HomeScreenEmptyMecanicoPreview() {
    PruebaKotlinTheme {
        HomeScreen(
            state = HomeUiState(
                usuario = Usuario.MecanicoUsuario(id = 1, nombre = "Carlos", email = "c@c.com"),
                vehicles = emptyList(),
                isLoading = false,
                error = null
            ),
            onEvent = {},
            onAddClick = {}
        )
    }
}

@Preview(showSystemUi = true, name = "Loading Skeleton")
@Composable
fun HomeScreenSkeletonPreview() {
    PruebaKotlinTheme {
        HomeScreen(
            state = HomeUiState(
                usuario = null,
                vehicles = emptyList(),
                isLoading = true,
                error = null
            ),
            onEvent = {},
            onAddClick = {}
        )
    }
}