package com.example.autolog.core.data.remote.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    
    private const val BASE_URL = "https://your-api-url.com/" // Cambiar por tu URL
    
    // Configuración de Gson para manejar fechas y valores nulos
    private val gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()
    
    // Configuración de logging interceptor para debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // Cliente HTTP con timeout configurado
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Instancia de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    // API de Cliente
    val clienteApi: ClienteApi by lazy {
        retrofit.create(ClienteApi::class.java)
    }
    
    // API de Mecánico
    val mecanicoApi: MecanicoApi by lazy {
        retrofit.create(MecanicoApi::class.java)
    }
    
    // API de Vehículo
    val vehiculoApi: VehiculoApi by lazy {
        retrofit.create(VehiculoApi::class.java)
    }
    
    /**
     * Permite cambiar la URL base en tiempo de ejecución
     */
    fun setBaseUrl(url: String): ApiClient {
        // Implementar si es necesario crear una nueva instancia de Retrofit
        return this
    }
}
