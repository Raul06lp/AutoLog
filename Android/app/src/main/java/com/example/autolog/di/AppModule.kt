package com.example.autolog.di

import com.example.autolog.core.data.remote.api.VehiculoApi
import com.example.autolog.domain.repository.FakeVehicleRepository
import com.example.autolog.domain.repository.VehiculoRepository
import com.example.autolog.domain.usecase.AddVehicleUseCase
import com.example.autolog.presentation.screens.add_vehicle.AddVehicleViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single<VehiculoRepository> { FakeVehicleRepository() }

    factory { AddVehicleUseCase(get()) }

    viewModel { AddVehicleViewModel(get()) }


    // Gson
    single<Gson> {
        GsonBuilder()
            .create()
    }

    // OkHttp
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://autolog-0mnd.onrender.com") // Cambia esto por tu URL de Render
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // APIs
    single<VehiculoApi> { get<Retrofit>().create(VehiculoApi::class.java) }

    // Repositories
    single<VehiculoRepository> { FakeVehicleRepository() }

    // Use Cases
    factory { AddVehicleUseCase(get()) }

    // ViewModels
    viewModel { AddVehicleViewModel(get()) }
}