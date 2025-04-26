package com.example.livestreamusingmvvm.ui.view.ui.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.livestreamusingmvvm.ui.view.ui.theme.LivestreamUsingmvvmTheme
import android.Manifest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import io.antmedia.webrtcandroidframework.core.WebRTCClient
import org.webrtc.EglBase
import org.webrtc.SurfaceViewRenderer

class GoLiveActivityCompose : ComponentActivity() {

    private lateinit var webRTCClient: WebRTCClient
    private var isStreaming by mutableStateOf(false)
    private var isRendererInitialized = false

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

        setContent {
            LivestreamUsingmvvmTheme {
                GoLiveScreen(
                    surfaceViewRenderer = surfaceViewRenderer,
                    isStreaming = isStreaming,
                    onStartClick = {
                        if (!isStreaming) {
                            webRTCClient = IWebRTCClient.builder()
                                .setActivity(this)
                                .setLocalVideoRenderer(surfaceViewRenderer)
                                .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")
                                .build()

                            webRTCClient.init()
                            webRTCClient.publish("stream1")

                            isStreaming = true
                        }
                    },
                    onStopClick = {
                        if (isStreaming) {
                            webRTCClient.stop("stream1")
                            isStreaming = false
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
        val eglBase = EglBase.create()

        if (!isRendererInitialized) {
            // surfaceViewRenderer.init(eglBase.eglBaseContext, null)
            surfaceViewRenderer.setZOrderMediaOverlay(true)
            surfaceViewRenderer.setEnableHardwareScaler(true)
            isRendererInitialized = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isStreaming) {
            webRTCClient.stop("stream1")
        }
    }
}


@Composable
fun GoLiveScreen(
    surfaceViewRenderer: SurfaceViewRenderer,
    isStreaming: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AndroidView(
            factory = { surfaceViewRenderer },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )

        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isStreaming
        ) {
            Text("Start Streaming")
        }

        Button(
            onClick = onStopClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isStreaming
        ) {
            Text("Stop Streaming")
        }

        Text(
            text = "Status: ${if (isStreaming) "Publishing..." else "Stopped"}",
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoLiveScreenPreview() {
    val context = LocalContext.current
    // Use a dummy SurfaceViewRenderer to avoid preview crash
    val dummyRenderer = remember {
        SurfaceViewRenderer(context).apply {
            // No need to initialize, just for preview placeholder
        }
    }

    GoLiveScreen(
        surfaceViewRenderer = dummyRenderer,
        isStreaming = false,
        onStartClick = {},
        onStopClick = {}
    )
}