package com.example.livestreamusingmvvm.ui.view

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamusingmvvm.databinding.ActivityPublishViewBindingBinding
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import io.antmedia.webrtcandroidframework.core.WebRTCClient
import org.webrtc.EglBase

class PublishStreamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublishViewBindingBinding
    private lateinit var webRTCClient: WebRTCClient
    private var isStreaming = false
    private var isRendererInitialized = false

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        if (granted) {
            initStreaming()
        } else {
            Toast.makeText(this, "Camera & Microphone permission required", Toast.LENGTH_LONG).show()
            binding.btnStart.isEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishViewBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()

        binding.btnStart.setOnClickListener {
            if (!isStreaming) {
// Initialize WebRTCClient with correct WebSocket URL for publishing
                 webRTCClient = IWebRTCClient.builder()
                    .setActivity(this)  // Context (Activity)
                    .setLocalVideoRenderer(binding.publisherView)  // Local video renderer
                    .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")  // Correct WebSocket URL for publishing
                    .build()

// Initialize WebRTC client
                webRTCClient?.init()

// Start publishing the stream
                webRTCClient.publish("stream1")  // Use your stream name as needed


                updateStatus("Publishing...")
                isStreaming = true
            }
        }

        binding.btnStop.setOnClickListener {
            if (isStreaming) {
                webRTCClient.stop("stream1")
                updateStatus("Stopped")
                isStreaming = false
            }
        }
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    private fun initStreaming() {
        val eglBase = EglBase.create()

        if (!isRendererInitialized) {
           // binding.publisherView.init(eglBase.eglBaseContext, null)
            binding.publisherView.setZOrderMediaOverlay(true)
            binding.publisherView.setEnableHardwareScaler(true)
            isRendererInitialized = true
        }
// Or webRTCClient.init(WebRTCClient.MODE_PUBLISH) if that is correct
// Step 3: Update status
        updateStatus("Ready to publish")

    }

    private fun updateStatus(status: String) {
        binding.tvStatus.text = "Status: $status"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isStreaming) {
            webRTCClient.stop("stream1")
        }
    }
}
