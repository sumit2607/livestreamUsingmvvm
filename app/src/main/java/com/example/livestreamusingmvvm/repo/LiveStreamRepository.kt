package com.example.livestreamusingmvvm.repo

import com.example.livestreamusingmvvm.remote.AntMediaApiService
import com.example.livestreamusingmvvm.remote.LiveStream
class LiveStreamRepository(private val apiService: AntMediaApiService) {

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
