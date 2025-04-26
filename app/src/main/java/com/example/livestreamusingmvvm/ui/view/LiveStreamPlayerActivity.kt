package com.example.livestreamusingmvvm.ui.view


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.livestreamusingmvvm.databinding.ActivityLiveStreamPlayerBinding
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import io.antmedia.webrtcandroidframework.core.WebRTCClient
import org.webrtc.EglBase


class LiveStreamPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveStreamPlayerBinding
    private var player: ExoPlayer? = null
    var webRTCClient : WebRTCClient? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLiveStreamPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val eglBase = EglBase.create()
       //binding.playerview.init(eglBase.eglBaseContext, null)

        // Initialize WebRTCClient with WebSocket URL for playing stream
         webRTCClient = IWebRTCClient.builder()
            .setActivity(this)  // Context (Activity)
            .addRemoteVideoRenderer(binding.playerview)  // Remote video renderer (for playing the stream)
            .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")  // Correct WebSocket URL for WebRTC
            .build()

// Initialize the client
        webRTCClient!!.init()

// Play the stream with the given stream ID
        webRTCClient!!.play("stream1")



        // Get streamId from Intent
        val streamId = intent.getStringExtra("streamId") ?: ""
        if (streamId.isNotEmpty()) {
            // Fetch live stream details for the given streamId
            fetchLiveStreamDetails(streamId)
        } else {
            showError("Stream ID is missing!")
        }
    }

    private fun fetchLiveStreamDetails(streamId: String) {
        // Show loading state
        showLoading(true)

        // Simulate a network call or data retrieval (e.g., from a local source or API)
        // Replace this with actual API call or data fetch logic
        // Here, we mock the response with hardcoded values
        Thread {
            try {
                // Simulate network delay or data fetch
                Thread.sleep(1000)
                // Mock data for this example
                val streamUrl = "rtmp://antmedia.workuplift.com/WebRTCAppEE/streamId_BWaMYQ3Yb"
                val streamRole = "Live Stream Role"
                val streamType = "Live"

                // Update UI on main thread
                runOnUiThread {
                    binding.streamTitle.text = streamRole
                    binding.streamStatus.text = streamType
                    setupPlayer(streamUrl)
                    showLoading(false)
                }
            } catch (e: Exception) {
                // Handle network or data fetching errors
                runOnUiThread {
                    showLoading(false)
                    showError("Error fetching stream details: ${e.message}")
                }
            }
        }.start()
    }

    private fun setupPlayer(streamUrl: String) {
        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build().apply {
            val mediaItem = MediaItem.fromUri(streamUrl)
            setMediaItem(mediaItem)
            prepare()
            play()
        }

        // Bind the player to the PlayerView in your layout
        //binding.playerView.player = player


    }

    private fun showLoading(isLoading: Boolean) {
        // Show or hide loading spinner based on loading state
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(error: String) {
        // Display error message (Toast)
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        // Optionally, show error in the UI
        binding.errorMessage.text = error
        binding.errorMessage.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        // Release the player when the activity is stopped
        player?.release()
    }
}
