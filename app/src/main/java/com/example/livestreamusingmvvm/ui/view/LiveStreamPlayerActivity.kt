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
    var webRTCClient : WebRTCClient? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityLiveStreamPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    override fun onStop() {
        super.onStop()
        // Release the player when the activity is stopped
    }
}
