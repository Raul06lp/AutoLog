package com.carlafdez.autolog.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object LoginKey : NavKey

@Serializable
object RegisterKey : NavKey

@Serializable
object VehicleListKey : NavKey

@Serializable
data class VehicleDetailKey(val vehicleId: Long) : NavKey

@Serializable
object AddVehicleKey : NavKey

@Serializable
data class EditVehicleKey(val vehicleId: Long) : NavKey
