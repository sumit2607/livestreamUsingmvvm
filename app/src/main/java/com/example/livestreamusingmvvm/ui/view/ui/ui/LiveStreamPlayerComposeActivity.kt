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
import com.example.livestreamusingmvvm.ui.view.ui.ui.theme.LivestreamUsingmvvmTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import io.antmedia.webrtcandroidframework.core.WebRTCClient

import org.webrtc.EglBase
import org.webrtc.SurfaceViewRenderer


class LiveStreamPlayerComposeActivity : ComponentActivity() {

    private var webRTCClient: WebRTCClient? = null
    private lateinit var surfaceViewRenderer: SurfaceViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init EGL and Surface Renderer
        val eglBase = EglBase.create()
        surfaceViewRenderer = SurfaceViewRenderer(this).apply {
            init(eglBase.eglBaseContext, null)
        }

        // WebRTC Client
        webRTCClient = IWebRTCClient.builder()
            .setActivity(this)
            .addRemoteVideoRenderer(surfaceViewRenderer)
            .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")
            .build()

        webRTCClient!!.init()

        val streamId = intent.getStringExtra("streamId") ?: "stream1"
        val streamRole = intent.getStringExtra("streamRole") ?: "Host"
        val streamType = intent.getStringExtra("streamType") ?: "Live"

        webRTCClient!!.play(streamId)

        setContent {
            LivestreamUsingmvvmTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StreamPlayerScreen(
                        surfaceViewRenderer = surfaceViewRenderer,
                        streamRole = streamRole,
                        streamType = streamType
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webRTCClient?.stop("")
        surfaceViewRenderer.release()
    }
}

@Composable
fun StreamPlayerScreen(
    surfaceViewRenderer: SurfaceViewRenderer,
    streamRole: String,
    streamType: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { surfaceViewRenderer },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Role: $streamRole", style = MaterialTheme.typography.titleMedium)
        Text(text = "Status: $streamType", style = MaterialTheme.typography.titleSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun StreamPlayerScreenPreview() {
    LivestreamUsingmvvmTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Mock SurfaceViewRenderer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Live Stream Preview",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Role: Host", style = MaterialTheme.typography.titleMedium)
            Text(text = "Status: Live", style = MaterialTheme.typography.titleSmall)
        }
    }
}
