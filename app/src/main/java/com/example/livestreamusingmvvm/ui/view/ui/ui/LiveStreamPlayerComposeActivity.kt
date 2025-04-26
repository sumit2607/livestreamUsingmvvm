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
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.livestreamusingmvvm.R


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*


class LiveStreamPlayerComposeActivity : ComponentActivity() {
    private lateinit var surfaceViewRenderer: SurfaceViewRenderer
    private var webRTCClient: WebRTCClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LiveStreamPlayerScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LiveStreamPlayerScreen() {
        val context = LocalContext.current

        Box(modifier = Modifier.fillMaxSize()) {
            // Fullscreen Video Player
            AndroidView(
                factory = { context ->
                    surfaceViewRenderer = SurfaceViewRenderer(context)
                    surfaceViewRenderer.apply {
                        // Any SurfaceViewRenderer setup
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Overlay Top-Left UI
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

                Text(
                    text = "Sumit Rai",
                    color = Color.White,
                    fontSize = 14.sp
                )

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

                Spacer(modifier = Modifier.height(8.dp))
            }

            // LIVE Icon with Viewer Count at Top-Right (proper horizontal spacing)
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Red, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp) // 👈 this creates horizontal spacing between items
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


            // Love (Heart) Icon above Gift Tray (Bottom-Right)
            Icon(
                imageVector = Icons.Default.Favorite, // Heart Icon
                contentDescription = "Love Icon",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 150.dp) // Positioned above gift tray
                    .size(50.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(12.dp)
            )

            // Other UI items like Love Icon etc.
            // Gifts Row at Bottom
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
                    Pair(Icons.Default.ShoppingCart, "Shopping Cart Gift"),
                    Pair(Icons.Default.Face, "Face Gift"),
                    Pair(Icons.Default.Lock, "Lock"),
                    Pair(Icons.Default.Favorite, "Favorite Gift"),
                    Pair(Icons.Default.Star, "Star Gift"),
                    Pair(Icons.Default.Star, "Star Gift"),
                    Pair(Icons.Default.Star, "Star Gift")
                )

                giftItems.forEach { (icon, name) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Gift: $name",
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

            // TextField with WhatsApp icon and "Share Now" text at the bottom right
            var messageText by remember { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart) // Align at the bottom right
                    .padding(
                        end = 16.dp,
                        bottom = 10.dp
                    ) // Adjust positioning from right and bottom
                    .width(400.dp), // Control the width of the row
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TextField (Say Hi!)
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Say Hi!") },
                    modifier = Modifier
                        .weight(1f) // Take available space
                        .height(50.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // background color
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = CircleShape,
                    singleLine = true
                )

                // WhatsApp Icon
                IconButton(
                    onClick = { /* WhatsApp sharing action */ },
                    modifier = Modifier
                        .padding(start = 8.dp) // Space between text field and WhatsApp icon
                ) {
                    Icon(
                        imageVector = Icons.Default.Share, // Use Share icon if WhatsApp icon is not available
                        contentDescription = "WhatsApp Share",
                        tint = Color.Green,
                        modifier = Modifier.size(30.dp) // Icon size
                    )
                }

                // "Share Now" Text
                Text(
                    text = "Share Now",
                    color = Color.Green,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 8.dp) // Space between icon and text
                        .clickable { /* Handle share action */ }
                )


            }
        }

        // Initialize WebRTC client when the screen is displayed
        LaunchedEffect(Unit) {
            webRTCClient = IWebRTCClient.builder()
                .setActivity(this@LiveStreamPlayerComposeActivity)
                .addRemoteVideoRenderer(surfaceViewRenderer)
                .setServerUrl("wss://antmedia.workuplift.com:5443/WebRTCAppEE/websocket")
                .build()

            webRTCClient!!.init()
            webRTCClient!!.play("streamId_fwoItPDuC")
        }
    }

    override fun onStop() {
        super.onStop()
        webRTCClient?.stop("streamId_fwoItPDuC")
    }
}
