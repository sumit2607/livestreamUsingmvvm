package com.example.livestreamusingmvvm.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// AntMediaApiService.kt
interface AntMediaApiService {
    // Updated endpoint to fetch live stream list with pagination (0 to 50)
    @GET("rest/v2/broadcasts/list/0/50")
    suspend fun getLiveStreams(): Response<List<LiveStream>>

    // Endpoint to fetch details of a particular stream
    @GET("api/stream/{streamId}")
    suspend fun getStreamDetails(@Path("streamId") streamId: String): Response<LiveStream>
}
