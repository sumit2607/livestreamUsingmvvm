package com.example.livestreamusingmvvm.ui.screen


import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.razorpay.PaymentResultListener
import io.antmedia.webrtcandroidframework.api.IWebRTCClient
import org.json.JSONObject
import org.webrtc.SurfaceViewRenderer
import android.util.Log
import com.google.firebase.events.Publisher
import io.antmedia.webrtcandroidframework.core.WebRTCClient

import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.webrtc.MediaStream


class LiveStreamPlayerComposeActivity : ComponentActivity() , PaymentResultListener {

    private lateinit var surfaceViewRenderer: SurfaceViewRenderer
    private var webRTCClient: IWebRTCClient? = null
    var streamId = " "
    private var monitoringRunnable: Runnable? = null


    private var isInPipMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        streamId = intent.getStringExtra("streamId") ?: "defaultStreamId"
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

                // LIVE Info + Join Co-host Top-Right
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            //sendRequestToBecomeCoHost(streamId)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Join as Co-host", fontSize = 12.sp)
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
                                    .clickable {
                                        startPayment()
                                    }
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

                webRTCClient?.play(streamId)
        }
    }




//    private fun startStreamMonitoring(context: Context) {
//        val handler = Handler(Looper.getMainLooper())
//        val checkInterval = 5000L
//
//        monitoringRunnable = object : Runnable {
//            override fun run() {
//                if (webRTCClient == null || !webRTCClient!!.isStreaming(streamId)) {
//                    stopStreamAndFinish(context)
//                } else {
//                    handler.postDelayed(this, checkInterval)
//                }
//            }
//        }
//
//        handler.postDelayed(monitoringRunnable!!, checkInterval)
//    }


//    private fun stopStreamAndFinish(context: Context) {
//        if(webRTCClient!=null)
//        webRTCClient?.stop(streamId)
//        Toast.makeText(context, "Live show ended by author", Toast.LENGTH_SHORT).show()
//        finish()
//    }

    override fun onStop() {
        super.onStop()
//        monitoringRunnable?.let {
//            Handler(Looper.getMainLooper()).removeCallbacks(it)  // Stop the monitoring
//        }
//        stopStreamAndFinish(this)
//        if(webRTCClient!=null)
//        webRTCClient?.stop(streamId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //stopStreamAndFinish(this)
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

    private fun startPayment() {
//        val checkout = Checkout()
//
//        // Set your test Key ID here
//        checkout.setKeyID("rzp_test_J3xPLh7xTbCTjc") // Replace with your actual Test Key ID
//
//        try {
//            // Create the options for the payment
//            val options = JSONObject()
//
//            // Payment details
//            options.put("name", "Your Company Name") // Your company name
//            options.put("description", "Purchase Description") // Description of the product/service
//            options.put("currency", "INR") // Currency INR
//            options.put("amount", "1000") // Amount in paise (1000 paise = ₹10)
//
//            // Pre-fill customer information
//            options.put("prefill.email", "customer@example.com") // Customer's email
//            options.put("prefill.contact", "1234567890") // Customer's phone number
//
//            // Open Razorpay Checkout
//            checkout.open(this, options)
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://rzp.io/rzp/IKikUVX"))
        startActivity(intent)

    }

    override fun onPaymentSuccess(p0: String?) {

    }

    override fun onPaymentError(p0: Int, p1: String?) {

    }


}



