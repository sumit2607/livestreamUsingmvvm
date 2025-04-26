package com.example.livestreamusingmvvm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livestreamusingmvvm.model.LiveStream
import com.example.livestreamusingmvvm.repository.LiveStreamRepository
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LiveStreamViewModel @Inject constructor(
    private val repository: LiveStreamRepository
) : ViewModel() {

    // Live streams list for the list fragment
    private val _liveStreams = MutableLiveData<List<LiveStream>?>()
    val liveStreams: LiveData<List<LiveStream>?> = _liveStreams

    // Live stream details for the player fragment
    private val _liveStreamDetails = MutableLiveData<LiveStream?>()
    val liveStreamDetails: LiveData<LiveStream?> = _liveStreamDetails

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    // Fetch the list of live streams
    fun fetchLiveStreams() {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val streams = repository.getLiveStreams()
                if (streams != null && streams.isNotEmpty()) {
                    _liveStreams.value = streams
                } else {
                    _errorState.value = "No live streams available."
                }
            } catch (e: Exception) {
                _errorState.value = "Error fetching live streams: ${e.message}"
            }
            _loadingState.value = false
        }
    }

    // Fetch the details of a specific live stream by its ID
    fun fetchLiveStreamDetails(streamId: String) {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                val streamDetails = repository.getLiveStreamDetails(streamId)
                if (streamDetails != null) {
                    _liveStreamDetails.value = streamDetails
                } else {
                    _errorState.value = "Live stream details not found."
                }
            } catch (e: Exception) {
                _errorState.value = "Error fetching live stream details: ${e.message}"
            }
            _loadingState.value = false
        }
    }
}
