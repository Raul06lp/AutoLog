package com.carlafdez.autolog.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.carlafdez.autolog.presentation.screens.addScreen.AddRoute
import com.carlafdez.autolog.presentation.screens.detailScreen.DetailRoute
import com.carlafdez.autolog.presentation.screens.editScreen.EditRoute
import com.carlafdez.autolog.presentation.screens.homeScreen.HomeRoute
import com.carlafdez.autolog.presentation.screens.loginScreen.LoginRoute
import com.carlafdez.autolog.presentation.screens.registerScreen.RegisterRoute

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(LoginKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<LoginKey> {
                LoginRoute(
                    onLoginSuccess = {
                        backStack.removeLastOrNull()
                        backStack.add(VehicleListKey)
                    },
                    onNavigateToRegister = { backStack.add(RegisterKey) }
                )
            }

            entry<RegisterKey> {
                RegisterRoute(
                    onRegisterSuccess = {
                        backStack.removeLastOrNull()
                        backStack.add(VehicleListKey)
                    },
                    onNavigateToLogin = { backStack.removeLastOrNull() }
                )
            }

            entry<VehicleListKey> {
                HomeRoute(
                    onVehicleClick = { id -> backStack.add(VehicleDetailKey(id)) },
                    onAddClick = { backStack.add(AddVehicleKey) }
                )
            }

            entry<VehicleDetailKey> { key ->
                DetailRoute(
                    vehiculoId = key.vehicleId,
                    onBack = { backStack.removeLastOrNull() },
                    onEditClick = { backStack.add(EditVehicleKey(key.vehicleId)) }
                )
            }

            entry<AddVehicleKey> {
                AddRoute(onBack = { backStack.removeLastOrNull() })
            }

            entry<EditVehicleKey> { key ->
                EditRoute(
                    vehiculoId = key.vehicleId,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
