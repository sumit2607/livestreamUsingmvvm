package com.example.livestreamusingmvvm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livestreamusingmvvm.model.LiveStream
import com.example.livestreamusingmvvm.repository.LiveStreamRepository
import com.example.livestreamusingmvvm.ui.intent.LiveStreamsIntent
import com.example.livestreamusingmvvm.ui.state.LiveStreamsState
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveStreamViewModel @Inject constructor(
    private val repository: LiveStreamRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LiveStreamsState())
    val state: StateFlow<LiveStreamsState> = _state.asStateFlow()

    fun handleIntent(intent: LiveStreamsIntent) {
        when (intent) {
            is LiveStreamsIntent.FetchLiveStreams -> {
                fetchLiveStreams()
            }
            is LiveStreamsIntent.RefreshLiveStreams -> {
                fetchLiveStreams(forceRefresh = intent.forceRefresh)
            }
            is LiveStreamsIntent.FetchLiveStreamDetails -> {
                fetchLiveStreamDetails(intent.streamId)
            }
        }
    }

    private fun fetchLiveStreams(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val streams = repository.getLiveStreams()
                if (!streams.isNullOrEmpty()) {
                    _state.value = _state.value.copy(
                        liveStreams = streams,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = ""
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error fetching live streams: ${e.message}"
                )
            }
        }
    }

    private fun fetchLiveStreamDetails(streamId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val details = repository.getLiveStreamDetails(streamId)
                if (details != null) {
                    _state.value = _state.value.copy(
                        liveStreamDetails = details,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Live stream details not found."
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error fetching live stream details: ${e.message}"
                )
            }
        }
    }
}
