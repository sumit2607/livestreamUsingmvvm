package com.example.livestreamusingmvvm.repository

import com.example.livestreamusingmvvm.network.AntMediaApiService
import com.example.livestreamusingmvvm.model.LiveStream
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class LiveStreamRepository @Inject constructor(
    private val apiService: AntMediaApiService
) {

    // Fetch live stream list
    suspend fun getLiveStreams(): List<LiveStream>? {
        return try {
            val response = apiService.getLiveStreams()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    // Fetch a specific live stream's details
    suspend fun getLiveStreamDetails(streamId: String): LiveStream? {
        return try {
            val response = apiService.getStreamDetails(streamId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}
