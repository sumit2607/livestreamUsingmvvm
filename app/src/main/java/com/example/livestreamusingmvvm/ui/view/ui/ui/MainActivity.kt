package com.example.livestreamusingmvvm.ui.view.ui.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamusingmvvm.network.NetworkModule
import com.example.livestreamusingmvvm.repository.LiveStreamRepository
import com.example.livestreamusingmvvm.ui.viewmodel.LiveStreamViewModel

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.livestreamusingmvvm.model.LiveStream
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.livestreamusingmvvm.R
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: LiveStreamViewModel by viewModels() // Automatically injected by Hilt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(
                        WindowInsets.systemBars
                            .asPaddingValues()
                    )
            ) {
                LiveStreamsListScreen()
            }
        }
    }
}

@Composable

fun LiveStreamsListScreen() {
    // Get the viewModel using Hilt's viewModels delegate
    val viewModel: LiveStreamViewModel = hiltViewModel()

    val liveStreams by viewModel.liveStreams.observeAsState(emptyList())
    val errorState by viewModel.errorState.observeAsState("")
    val loadingState by viewModel.loadingState.observeAsState(false)

    LaunchedEffect(Unit) {
        // Fetch the live streams when the screen is launched
        viewModel.fetchLiveStreams()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // Show error message if there's any
            if (errorState.isNotEmpty()) {
                Text(
                    text = errorState,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Display the list of live streams
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(liveStreams ?: emptyList()) { stream ->
                    LiveStreamItem(stream = stream)
                }
            }
        }

        // Show loading indicator if the state is true
        if (loadingState) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun LiveStreamItem(stream: LiveStream) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF0F0F0))
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.liveshow),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = stream.streamId, fontWeight = FontWeight.Bold)
        Text(text = stream.status, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                val intent = Intent(context, LiveStreamPlayerComposeActivity::class.java)
                intent.putExtra("streamId", stream.streamId)
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Play")
        }
    }
}


