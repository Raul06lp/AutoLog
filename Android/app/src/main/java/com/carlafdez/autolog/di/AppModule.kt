package com.carlafdez.autolog.di

import com.carlafdez.autolog.data.remote.api.VehiculoApi
import com.carlafdez.autolog.data.repository.VehiculoRepositoryImpl
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import com.carlafdez.autolog.presentation.screens.addScreen.AddViewModel
import com.carlafdez.autolog.presentation.screens.detailScreen.VehicleDetailViewModel
import com.carlafdez.autolog.presentation.screens.editScreen.EditViewModel
import com.carlafdez.autolog.presentation.screens.homeScreen.HomeViewModel
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", Credentials.basic("autolog", "X9#mK2\$vQpL7@nRw"))
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://autolog-0mnd.onrender.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<VehiculoApi> { get<Retrofit>().create(VehiculoApi::class.java) }

    single<VehiculoRepository> { VehiculoRepositoryImpl(get<VehiculoApi>(), androidContext()) }

    viewModel { HomeViewModel(get<VehiculoRepository>()) }
    viewModel { (vehiculoId: Long) -> VehicleDetailViewModel(vehiculoId, get<VehiculoRepository>()) }
    viewModel { AddViewModel(get<VehiculoRepository>()) }
    viewModel { (vehiculoId: Long) -> EditViewModel(vehiculoId, get<VehiculoRepository>()) }
}
