package com.example.autolog.presentation.screens.home_vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autolog.presentation.components.CarCard
import com.example.autolog.domain.model.Vehicle
import com.example.autolog.ui.theme.AutoLogTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User,
    vehicles: List<Vehicle>,
    onCarDetailsClick: (String) -> Unit = {},
    onCarCompleteClick: (String) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "AutoLog",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = true,
                        onClick = {
                            scope.launch { drawerState.close() }
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Mis Coches") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                        }
                    )

                    if (user::class.simpleName == "Mecanico") {
                        NavigationDrawerItem(
                            label = { Text("Gestión") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                            }
                        )
                    }

                    NavigationDrawerItem(
                        label = { Text("Perfil") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "AutoLog",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1976D2),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(vehicles) { vehiculo ->
                    CarCard(
                        imagen = vehiculo.imagen,
                        cliente = vehiculo.cliente.name,
                        marca = vehiculo.marca,
                        modelo = vehiculo.modelo,
                        onDetailsClick = { onCarDetailsClick(vehiculo.id) },
                        onCompleteClick = if (user::class.simpleName == "Mecanico") {
                            { onCarCompleteClick(vehiculo.id) }
                        } else null
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    AutoLogTheme {
        val user = User.Mecanico(
            email = "",
            name = "Carla Fernández",
            id = 123
        )

        val cochesDemo = listOf(
            Vehicle(
                id = "1",
                imagen = null,
                cliente = user,
                marca = "Mazda",
                modelo = "Miata MX5",
                matricula = "",
                year = 2000,
                kilometros = "",
                color = "",
                observaciones = ""

            ),
            Vehicle(
                id = "2",
                imagen = null,
                cliente = user,
                marca = "Toyota",
                modelo = "Supra",
                matricula = "",
                year = 2000,
                kilometros = "",
                color = "",
                observaciones = ""

            ),
            Vehicle(
                id = "3",
                imagen = null,
                cliente = user,
                marca = "Honda",
                modelo = "Civic Type R",
                matricula = "",
                year = 2000,
                kilometros = "",
                color = "",
                observaciones = ""

            ),
            Vehicle(
                id = "4",
                imagen = null,
                cliente = user,
                marca = "Nissan",
                modelo = "GT-R R35",
                matricula = "",
                year = 2000,
                kilometros = "",
                color = "",
                observaciones = ""

            )
        )

        HomeScreen(
            user = user,
            vehicles = cochesDemo
        )
    }
}