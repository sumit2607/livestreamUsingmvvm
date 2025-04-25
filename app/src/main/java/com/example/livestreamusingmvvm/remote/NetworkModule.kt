package com.example.livestreamusingmvvm.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// NetworkModule.kt (Optional if using Dependency Injection like Dagger or Hilt)
object NetworkModule {
    // Updated base URL with server address, port, and application name
    private const val BASE_URL = "https://antmedia.workuplift.com:5443/WebRTCAppEE/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: AntMediaApiService = retrofit.create(AntMediaApiService::class.java)
}