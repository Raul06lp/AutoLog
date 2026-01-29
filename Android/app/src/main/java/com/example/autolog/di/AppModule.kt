package com.example.autolog.di

import com.example.autolog.feature.vehicle.domain.repository.FakeVehicleRepository
import com.example.autolog.feature.vehicle.domain.repository.VehiculoRepository
import com.example.autolog.feature.vehicle.domain.usecase.AddVehicleUseCase
import com.example.autolog.feature.vehicle.presentation.add_car.AddCarViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<VehiculoRepository> { FakeVehicleRepository() }

    factory { AddVehicleUseCase(get()) }

    viewModel { AddCarViewModel(get()) }

}