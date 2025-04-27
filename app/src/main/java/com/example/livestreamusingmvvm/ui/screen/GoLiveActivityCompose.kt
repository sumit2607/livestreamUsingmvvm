package com.example.livestreamusingmvvm.ui.screen

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
import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.livestreamusingmvvm.ui.screen.theme.LivestreamUsingmvvmTheme
import org.webrtc.MediaStream
import kotlin.random.Random


class GoLiveActivityCompose : ComponentActivity() {

    private lateinit var webRTCClient: WebRTCClient
    private var isStreaming by mutableStateOf(true)  // Start streaming automatically
    private var isAudioMuted by mutableStateOf(false)
    private var isVideoStopped by mutableStateOf(false)

    private lateinit var surfaceViewRenderer: SurfaceViewRenderer
    var streamPublishId = ""

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

        streamPublishId =    generateRandomString()

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
                             webRTCClient.setAudioEnabled(false) // Unmute as required
                        } else {
                            webRTCClient.setAudioEnabled(true) // Unmute as requiredd
                        }
                    },
                    onStopVideoClick = {
                        isVideoStopped = !isVideoStopped
                        if (isVideoStopped) {
                             webRTCClient.setVideoEnabled(false)
                        } else {
                            webRTCClient.setVideoEnabled(true)
                        }
                    }
                )
            }
        }
    }

   private fun generateRandomString(): String {
        val number = Random.nextInt(1000)  // Generates a random number between 0 and 999
        val randomLetters = (1..3)
            .map { ('a'..'z').random() }  // Randomly selecting 3 letters
            .joinToString("")

        return "stream$number$randomLetters"
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
        webRTCClient.publish(streamPublishId)
        isStreaming = true
    }

    private fun stopStream() {
        if (isStreaming) {
            webRTCClient.stop(streamPublishId)
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
@OptIn(ExperimentalMaterial3Api::class)
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
    val context = LocalContext.current
    var chatMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Preview
        AndroidView(
            factory = { surfaceViewRenderer },
            modifier = Modifier.fillMaxSize()
        )

        // --- Top Left: User Info ---
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sumit Rai",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "You are Live",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        // --- Top Right: Watching Count + Close Button ---
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Count Text with background from drawable
            Text(
                text = "200 Watching",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .background(
                        color = Color(0xB0F12121), // 50% Black
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )


            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    onStopClick() // Call to stop streaming
                    if (context is Activity) {
                        context.finish() // Close the current activity
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Stop Streaming",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // --- Middle Right: Audio and Video Controls ---
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onMuteClick,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isAudioMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeUp,
                    contentDescription = "Mute/Unmute Audio",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = onStopVideoClick,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isVideoStopped) Icons.Filled.VideocamOff else Icons.Filled.Videocam,
                    contentDescription = "Stop/Start Video",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        // --- Bottom Chat Field ---
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(25.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = chatMessage,
                onValueChange = { chatMessage = it },
                placeholder = { Text("Type and say hi to followers", color = Color.White.copy(alpha = 0.5f)) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor =  Color.White
                ),
                modifier = Modifier.weight(1f)
            )
        }

        // --- Status Text ---
        if (isAudioMuted || isVideoStopped) {
            Text(
                text = when {
                    isAudioMuted && isVideoStopped -> "Audio Muted & Camera Stopped"
                    isAudioMuted -> "Audio Muted"
                    isVideoStopped -> "Camera Stopped"
                    else -> ""
                },
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp),
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
