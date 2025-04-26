package com.example.livestreamusingmvvm.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// NetworkModule.kt (Optional if using Dependency Injection like Dagger or Hilt)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val baseUrl = "https://antmedia.workuplift.com:5443/WebRTCAppEE/" // Base URL
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAntMediaApiService(retrofit: Retrofit): AntMediaApiService {
        return retrofit.create(AntMediaApiService::class.java)
    }
}
