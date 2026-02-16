package com.carlafdez.pruebakotlin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.carlafdez.pruebakotlin.presentation.screens.homeScreen.HomeRoute

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(VehicleListKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<VehicleListKey> {
                HomeRoute(
                    onVehicleClick = { id ->
                        backStack.add(VehicleDetailKey(id))
                    },
                    onAddClick = {
                        backStack.add(AddVehicleKey)
                    }
                )
            }

            entry<VehicleDetailKey> { key ->
                // TODO: VehicleDetailRoute(vehicleId = key.vehicleId)
            }

            entry<AddVehicleKey> {
                // TODO: AddVehicleRoute(onBack = { backStack.removeLastOrNull() })
            }

        }
    )
}