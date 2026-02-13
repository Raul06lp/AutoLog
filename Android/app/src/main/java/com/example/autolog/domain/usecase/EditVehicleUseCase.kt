package com.example.autolog.domain.usecase

import com.example.autolog.domain.model.Vehicle
import com.example.autolog.domain.repository.VehiculoRepository
import java.util.Calendar


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
        return Calendar.getInstance().get(Calendar.YEAR)
    }
}