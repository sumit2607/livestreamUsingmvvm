package com.example.livestreamusingmvvm.ui.view.ui.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import io.antmedia.webrtcandroidframework.core.WebRTCClient
import org.webrtc.EglBase
import org.webrtc.SurfaceViewRenderer
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext


class LiveStreamPlayerComposeActivity : ComponentActivity() {
    private lateinit var surfaceViewRenderer: SurfaceViewRenderer
    private var webRTCClient: WebRTCClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LiveStreamPlayerScreen()
        }
    }

    @Composable
    fun LiveStreamPlayerScreen() {
        // Create and remember the WebRTC client instance
        val context = LocalContext.current

        // Using AndroidView to wrap SurfaceViewRenderer
        AndroidView(
            factory = { context ->
                surfaceViewRenderer = SurfaceViewRenderer(context)
                surfaceViewRenderer.apply {
                    // Any initial setup for SurfaceViewRenderer
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Initialize WebRTC client when the screen is displayed
        LaunchedEffect(Unit) {
            webRTCClient = IWebRTCClient.builder()
                .setActivity(this@LiveStreamPlayerComposeActivity) // Context (Activity)
                .addRemoteVideoRenderer(surfaceViewRenderer)  // Remote video renderer (for playing the stream)
                .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket") // Correct WebSocket URL for WebRTC
                .build()

            // Initialize the WebRTC client and start streaming
            webRTCClient!!.init()
            webRTCClient!!.play("stream1")
        }
    }

    override fun onStop() {
        super.onStop()
        // Release WebRTCClient resources if needed
        webRTCClient?.stop("stream1") // Assuming there's a release method
    }
}
