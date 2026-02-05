package com.example.autolog.feature.vehicle.domain.usecase

import com.example.autolog.feature.vehicle.domain.model.Vehicle
import com.example.autolog.feature.vehicle.domain.repository.VehiculoRepository


class EditVehicleUseCase (private val repository: VehiculoRepository){
    suspend operator fun invoke(vehicle: Vehicle) {

        if (vehicle.id.isBlank()) {
            throw IllegalArgumentException("El ID del vehículo no puede estar vacío")
        }

        if (vehicle.cliente.name.isBlank()) {
            throw IllegalArgumentException("El nombre del cliente es obligatorio")
        }

        if (vehicle.marca.isBlank()) {
            throw IllegalArgumentException("La marca es obligatoria")
        }

        if (vehicle.modelo.isBlank()) {
            throw IllegalArgumentException("El modelo es obligatorio")
        }

        if (vehicle.matricula.isBlank()) {
            throw IllegalArgumentException("Matricula vacia")
        }

        if (vehicle.year < 1900 || vehicle.year > getCurrentYear() + 1) {
            throw IllegalArgumentException("Año inválido. Debe estar entre 1900 y ${getCurrentYear() + 1}")
        }

        repository.updateVehicle(vehicle)
    }

    private fun getCurrentYear(): Int {
        return java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    }
}