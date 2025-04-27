package com.example.livestreamusingmvvm.ui.state


import com.example.livestreamusingmvvm.model.LiveStream

data class LiveStreamsState(
    val isLoading: Boolean = false,
    val liveStreams: List<LiveStream>? = emptyList(),
    val liveStreamDetails: LiveStream? = null,
    val error: String? = null
)
