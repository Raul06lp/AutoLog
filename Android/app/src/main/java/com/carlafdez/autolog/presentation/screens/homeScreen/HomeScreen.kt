package com.carlafdez.autolog.presentation.screens.homeScreen

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.alpha
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
import com.carlafdez.autolog.presentation.screens.homeScreen.components.AnimatedItem
import com.carlafdez.autolog.presentation.screens.homeScreen.components.CarCard
import com.carlafdez.autolog.presentation.screens.homeScreen.components.EmptyState
import com.carlafdez.autolog.presentation.screens.homeScreen.components.ErrorState
import com.carlafdez.autolog.presentation.screens.homeScreen.components.HomeHeader
import com.carlafdez.autolog.presentation.screens.homeScreen.components.SkeletonCard
import com.carlafdez.autolog.presentation.screens.homeScreen.components.vehiclePreviews
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme
import kotlinx.coroutines.delay


val NavyDeep   = Color(0xFF0F1E4E)
val BlueAccent = Color(0xFF2563EB)
private val BgLight    = Color(0xFFF4F6FB)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit
) {
    val isMecanico = state.usuario is Usuario.MecanicoUsuario

    Scaffold(
        containerColor = BgLight,
        floatingActionButton = {
            if (isMecanico) {
                FloatingActionButton(
                    onClick = { onEvent(HomeEvent.OnAddClick) },
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
                    HomeHeader(
                        usuario = state.usuario,
                        vehicleCount = state.vehicles.size.takeIf { !state.isLoading },
                        onProfileClick = { onEvent(HomeEvent.OnProfileClick) },
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
                                onAddClick = { onEvent(HomeEvent.OnAddClick) }
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
            onEvent = {}
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
            onEvent = {}
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
            onEvent = {}
        )
    }
}
