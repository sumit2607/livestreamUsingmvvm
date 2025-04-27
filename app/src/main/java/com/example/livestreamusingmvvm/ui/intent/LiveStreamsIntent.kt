package com.example.livestreamusingmvvm.ui.intent


import com.example.livestreamusingmvvm.model.LiveStream

sealed class LiveStreamsIntent {
    object FetchLiveStreams : LiveStreamsIntent()
    data class RefreshLiveStreams(val forceRefresh: Boolean = true) : LiveStreamsIntent()
    data class FetchLiveStreamDetails(val streamId: String) : LiveStreamsIntent()
}
