package com.example.autolog.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.autolog.feature.vehicle.presentation.add_car.AddCarRoute

@Composable
fun NavigationRoot(startRoute: NavKey= HomeKey) {

    val backStack = rememberNavBackStack(startRoute)

    NavDisplay(
        backStack = backStack,
        onBack = {  },
        entryProvider = entryProvider {
            entry<HomeKey> {

            }

            entry<AddCarKey> {
                AddCarRoute(
                    onBack = { backStack.removeLastOrNull() }
                )
            }

        }

    )



}