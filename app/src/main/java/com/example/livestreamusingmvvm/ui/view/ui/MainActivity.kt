package com.example.livestreamusingmvvm.ui.view.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.livestreamusingmvvm.ui.viewmodel.LiveStreamViewModel

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.livestreamusingmvvm.model.LiveStream
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.livestreamusingmvvm.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: LiveStreamViewModel by viewModels()
    private var showSplash by mutableStateOf(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            if (showSplash) {
                SplashScreen {
                    showSplash = false // After 3 seconds, show MainContent
                }
            } else {
                LiveStreamsListScreen()
            }
        }
    }

    @Composable
    fun LiveStreamsListScreen() {
        val viewModel: LiveStreamViewModel = hiltViewModel()

        val liveStreams by viewModel.liveStreams.observeAsState(emptyList())
        val errorState by viewModel.errorState.observeAsState("")
        val loadingState by viewModel.loadingState.observeAsState(false)

        var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

        LaunchedEffect(Unit) {
            viewModel.fetchLiveStreams()
        }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(selectedTab = selectedTab) {
                    selectedTab = it
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                when (selectedTab) {
                    BottomNavItem.Home -> liveStreams?.let { HomeScreen() }
                    BottomNavItem.LiveNow -> liveStreams?.let { LiveNowScreen() }
                    BottomNavItem.Profile -> ProfileScreen()
                    BottomNavItem.AllLiveShows -> {
                        // Fetch the live stream data from ViewModel or any source
                        liveStreams?.let { AllLiveShowsScreen(it, isLoading = false, onRefresh = {
                        /* refresh logic */
                            viewModel.fetchLiveStreams()
                        }) }
                    }

                }

                if (loadingState) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    enum class BottomNavItem(val label: String, val icon: ImageVector) {
        Home("Home", Icons.Default.Home),
        AllLiveShows("All Live Shows", Icons.Default.List),
        LiveNow("Go Live", Icons.Default.PlayArrow),
        Profile("Profile", Icons.Default.Person),

    }

    @Composable
    fun BottomNavigationBar(
        selectedTab: BottomNavItem,
        onTabSelected: (BottomNavItem) -> Unit
    ) {
        NavigationBar(
            containerColor = Color.White
        ) {
            BottomNavItem.values().forEach { item ->
                NavigationBarItem(
                    selected = selectedTab == item,
                    onClick = { onTabSelected(item) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        Text(text = item.label)
                    }
                )
            }
        }
    }

    @Composable
    fun HomeScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Home Screen", fontWeight = FontWeight.Bold)
        }
    }

    @Composable
    fun LiveNowScreen() {
        // You can filter streams which are actually live
        val context = LocalContext.current // Get the context
        val intent = Intent(context, GoLiveActivityCompose::class.java)
        context.startActivity(intent) // Launch the activity

    }

    @Composable
    fun ProfileScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Profile Screen", fontWeight = FontWeight.Bold)
        }
    }

    @Composable
    fun AllLiveShowsScreen(
        liveStreams: List<LiveStream>,
        isLoading: Boolean, // Pass this to manage loading state
        onRefresh: () -> Unit // Callback for swipe-to-refresh
    ) {
        // Swipe-to-refresh layout should wrap the scrollable content (LazyColumn)
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isLoading),
            onRefresh = onRefresh
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Display heading at the top center
                Text(
                    text = "Live Shows",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.Black), // Customize text style
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally) // Align to the top-center
                        .padding(top = 16.dp) // Adjust the top padding as per your need
                )

                if (liveStreams.isEmpty()) {
                    // Show Lottie Animation
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Replace this with your actual Lottie animation
                        LottieAnimation(
                            composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data_animation)).value,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                } else {
                    // Show the list
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(liveStreams) { stream ->
                            LiveStreamItem(stream = stream)
                        }
                    }
                }
            }
        }
    }




    @Preview(showBackground = true)
    @Composable
    fun LiveStreamsListScreenPreview() {
        LiveStreamsListScreen()
    }


    @Composable
    fun LiveStreamItem(stream: LiveStream?) {
        val context = LocalContext.current

        // Load the Lottie animation composition once
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data_new))
        val progress by animateLottieCompositionAsState(composition)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F0F0))
                .padding(10.dp)
        ) {
                // Show actual stream content if data is available
                Image(
                    painter = painterResource(id = R.drawable.liveshow),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

            stream?.streamId?.let { Text(text = it, fontWeight = FontWeight.Bold) }
            if (stream != null) {
                Text(text = stream.status, color = Color.DarkGray)
            }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, LiveStreamPlayerComposeActivity::class.java)
                        if (stream != null) {
                            intent.putExtra("streamId", stream.streamId)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Play")
                }
        }
    }

    @Composable
    fun SplashScreen(onSplashFinished: () -> Unit) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.live_now_new)) // Your Lottie JSON file in res/raw
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        LaunchedEffect(Unit) {
            delay(5000L) // Wait 3 seconds
            onSplashFinished()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .fillMaxSize()
                    .scale(2f)     // 📏 Scale up 20% to fill screen and remove space
            )

        }
    }

}



