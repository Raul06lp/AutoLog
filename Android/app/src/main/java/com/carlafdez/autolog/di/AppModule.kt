package com.carlafdez.autolog.di

import com.carlafdez.autolog.data.local.SessionManager
import com.carlafdez.autolog.data.remote.api.AuthApi
import com.carlafdez.autolog.data.remote.api.VehiculoApi
import com.carlafdez.autolog.data.repository.AuthRepositoryImpl
import com.carlafdez.autolog.data.repository.VehiculoRepositoryImpl
import com.carlafdez.autolog.domain.repository.AuthRepository
import com.carlafdez.autolog.domain.repository.VehiculoRepository
import com.carlafdez.autolog.presentation.screens.addScreen.AddViewModel
import com.carlafdez.autolog.presentation.screens.detailScreen.DetailViewModel
import com.carlafdez.autolog.presentation.screens.editScreen.EditViewModel
import com.carlafdez.autolog.presentation.screens.homeScreen.HomeViewModel
import com.carlafdez.autolog.presentation.screens.loginScreen.LoginViewModel
import com.carlafdez.autolog.presentation.screens.profileScreen.ProfileViewModel
import com.carlafdez.autolog.presentation.screens.registerScreen.RegisterViewModel
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
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
    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }
    single { SessionManager(androidContext()) }

    single<VehiculoRepository> { VehiculoRepositoryImpl(get<VehiculoApi>(), androidContext()) }
    single<AuthRepository> { AuthRepositoryImpl(get<AuthApi>(), get<SessionManager>()) }

    viewModel { HomeViewModel(get<VehiculoRepository>(), get<AuthRepository>()) }
    viewModel { (vehiculoId: Long) -> DetailViewModel(vehiculoId, get<VehiculoRepository>(), get<AuthRepository>()) }
    viewModel { AddViewModel(get<VehiculoRepository>(), get<AuthRepository>()) }
    viewModel { (vehiculoId: Long) -> EditViewModel(vehiculoId, get<VehiculoRepository>()) }
    viewModel { LoginViewModel(get<AuthRepository>()) }
    viewModel { RegisterViewModel(get<AuthRepository>()) }
    viewModel { ProfileViewModel(get<AuthRepository>()) }
}
