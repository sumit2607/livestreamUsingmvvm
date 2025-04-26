package com.example.livestreamusingmvvm.ui.view.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import io.antmedia.webrtcandroidframework.core.WebRTCClient
import org.webrtc.SurfaceViewRenderer
import android.Manifest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.ui.platform.LocalContext
import com.example.livestreamusingmvvm.ui.view.ui.theme.LivestreamUsingmvvmTheme



class GoLiveActivityCompose : ComponentActivity() {

    private lateinit var webRTCClient: WebRTCClient
    private var isStreaming by mutableStateOf(true)  // Start streaming automatically
    private var isAudioMuted by mutableStateOf(false)
    private var isVideoStopped by mutableStateOf(false)

    private lateinit var surfaceViewRenderer: SurfaceViewRenderer

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        if (granted) {
            initStreaming()
        } else {
            Toast.makeText(this, "Camera & Microphone permission required", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceViewRenderer = SurfaceViewRenderer(this)
        requestPermissions()

        // Automatically start streaming when the activity is created
        startStream()
//        if (!isStreaming) {
//            startStream()
//        }

        setContent {
            LivestreamUsingmvvmTheme {
                GoLiveScreen(
                    surfaceViewRenderer = surfaceViewRenderer,
                    isStreaming = isStreaming,
                    isAudioMuted = isAudioMuted,
                    isVideoStopped = isVideoStopped,
                    onStartClick = {
                        if (!isStreaming) {
                            startStream()
                        }
                    },
                    onStopClick = {
                        if (isStreaming) {
                            stopStream()
                        }
                    },
                    onMuteClick = {
                        isAudioMuted = !isAudioMuted
                        if (isAudioMuted) {
                            // webRTCClient.muteAudio() // Unmute as required
                        } else {
                            // webRTCClient.unmuteAudio() // Unmute as required
                        }
                    },
                    onStopVideoClick = {
                        isVideoStopped = !isVideoStopped
                        if (isVideoStopped) {
                            // webRTCClient.stopVideo()
                        } else {
                            // webRTCClient.startVideo()
                        }
                    }
                )
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
        // Initialize WebRTC client and other setups
        val eglBase = org.webrtc.EglBase.create()
        surfaceViewRenderer.setZOrderMediaOverlay(true)
        surfaceViewRenderer.setEnableHardwareScaler(true)
    }

    private fun startStream() {
        // Initialize WebRTC Client
        webRTCClient = IWebRTCClient.builder()
            .setActivity(this)
            .setLocalVideoRenderer(surfaceViewRenderer)
            .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")
            .build()

        webRTCClient.init()
        webRTCClient.publish("stream1")
        isStreaming = true
    }

    private fun stopStream() {
        if (isStreaming) {
            webRTCClient.stop("stream1")
            isStreaming = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isStreaming) {
            stopStream()
        }
    }
}

@Composable
fun GoLiveScreen(
    surfaceViewRenderer: SurfaceViewRenderer,
    isStreaming: Boolean,
    isAudioMuted: Boolean,
    isVideoStopped: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onMuteClick: () -> Unit,
    onStopVideoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Preview (always visible)
        AndroidView(
            factory = { surfaceViewRenderer },
            modifier = Modifier
                .fillMaxSize()
        )

        // Stop Streaming Button at Top Right Corner with Rounded Background
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)  // Adds some margin from the top and right edges
                .background(
                    color = Color.Black.copy(alpha = 0.5f),  // Semi-transparent background
                    shape = RoundedCornerShape(50)  // Rounded corners
                )
        ) {
            IconButton(
                onClick = onStopClick,
                modifier = Modifier
                    .size(50.dp)  // Make the button large enough for the rounded background
            ) {
                Icon(
                    imageVector = Icons.Default.Close,  // Default cross icon
                    contentDescription = "Stop Streaming",
                    tint = Color.White,  // White color for the icon
                    modifier = Modifier.size(24.dp)  // Set the icon size
                )
            }
        }

        // Floating Control Buttons aligned to the right middle
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)  // Space between buttons
        ) {
            // Mute and Stop Video Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center  // Center the buttons horizontally
            ) {
                // Mute Button
                IconButton(
                    onClick = onMuteClick,
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(50)  // Rounded button
                        )
                ) {
                    Icon(
                        imageVector = if (isAudioMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Mute/Unmute Audio",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Stop Video Button
                IconButton(
                    onClick = onStopVideoClick,
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(50)  // Rounded button
                        )
                ) {
                    Icon(
                        imageVector = if (isVideoStopped) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Stop/Start Video",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Status Text
            Text(
                text = when {
                    isAudioMuted && isVideoStopped -> "Audio Muted & Camera Stopped"
                    isAudioMuted -> "Audio Muted"
                    isVideoStopped -> "Camera Stopped"
                    else -> ""
                },
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GoLiveScreenPreview() {
    val context = LocalContext.current
    val dummyRenderer = remember {
        SurfaceViewRenderer(context).apply {
            // Placeholder, no need for initialization
        }
    }

    GoLiveScreen(
        surfaceViewRenderer = dummyRenderer,
        isStreaming = true,
        isAudioMuted = false,
        isVideoStopped = false,
        onStartClick = {},
        onStopClick = {},
        onMuteClick = {},
        onStopVideoClick = {}
    )
}
