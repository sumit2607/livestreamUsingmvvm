package com.example.livestreamusingmvvm.ui.screen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import org.webrtc.SurfaceViewRenderer
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*


import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi

class LiveStreamPlayerComposeActivity : ComponentActivity() {

    private lateinit var surfaceViewRenderer: SurfaceViewRenderer
    private var webRTCClient: IWebRTCClient? = null

    private var isInPipMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveStreamPlayerScreen()
        }
    }

    @kotlin.OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LiveStreamPlayerScreen() {
        val context = LocalContext.current
        var messageText by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {

            // WebRTC Video Surface
            AndroidView(
                factory = { ctx ->
                    surfaceViewRenderer = SurfaceViewRenderer(ctx).apply {
                        // Setup if required
                    }
                    surfaceViewRenderer
                },
                modifier = Modifier.fillMaxSize()
            )

            if (!isInPipMode) {
                // Profile UI Top-Left
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Sumit Rai", color = Color.White, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { /* Follow action */ },
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "Follow", fontSize = 12.sp)
                    }
                }

                // LIVE Info Top-Right
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.Red, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Live Icon",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Viewers Icon",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "200",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                // Heart Icon
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Love Icon",
                    tint = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 150.dp)
                        .size(50.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(12.dp)
                )

                // Gift Items
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 70.dp)
                        .align(Alignment.BottomCenter)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val giftItems = listOf(
                        Pair(Icons.Default.Star, "Star Gift"),
                        Pair(Icons.Default.Favorite, "Heart Gift"),
                        Pair(Icons.Default.Lock, "Lock Box Gift"),
                        Pair(Icons.Default.ShoppingCart, "Cart Gift"),
                        Pair(Icons.Default.Face, "Face Gift"),
                        Pair(Icons.Default.Lock, "Lock"),
                        Pair(Icons.Default.Favorite, "Fav Gift"),
                        Pair(Icons.Default.Star, "Star Gift"),
                        Pair(Icons.Default.Star, "Star Gift"),
                        Pair(Icons.Default.Star, "Star Gift")
                    )

                    giftItems.forEach { (icon, name) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = icon,
                                contentDescription = name,
                                tint = Color.Yellow,
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color.DarkGray, shape = CircleShape)
                                    .padding(12.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name,
                                color = Color.White,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Chat Box
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(end = 16.dp, bottom = 10.dp)
                        .width(400.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Say Hi!") },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = CircleShape,
                        singleLine = true
                    )

                    IconButton(
                        onClick = { /* WhatsApp share */ },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Green,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "Share Now",
                        color = Color.Green,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { /* Handle Share */ }
                    )
                }
            }
        }

        // Start WebRTC when screen is ready
        LaunchedEffect(Unit) {
            webRTCClient = IWebRTCClient.builder()
                .setActivity(this@LiveStreamPlayerComposeActivity)
                .addRemoteVideoRenderer(surfaceViewRenderer)
                .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")
                .build()

            webRTCClient?.play("stream1")
            startStreamMonitoring(context)
        }
    }

    private fun startStreamMonitoring(context: Context) {
        val handler = Handler(Looper.getMainLooper())
        val checkInterval = 5000L

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (webRTCClient == null || !webRTCClient!!.isStreaming("stream1")) {
                    stopStreamAndFinish(context)
                } else {
                    handler.postDelayed(this, checkInterval)
                }
            }
        }, checkInterval)
    }

    private fun stopStreamAndFinish(context: Context) {
        webRTCClient?.stop("stream1")
        Toast.makeText(context, "Live show ended by author", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onStop() {
        super.onStop()
        webRTCClient?.stop("streamId_lmhdVQiRR")
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(9, 16))
                .build()
            enterPictureInPictureMode(params)
        }
    }


    @RequiresApi(35)
    override fun onPictureInPictureUiStateChanged(pipState: PictureInPictureUiState) {
        super.onPictureInPictureUiStateChanged(pipState)
        isInPipMode = pipState.isTransitioningToPip
    }
}



