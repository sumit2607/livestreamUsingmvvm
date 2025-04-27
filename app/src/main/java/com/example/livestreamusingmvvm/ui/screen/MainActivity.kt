package com.example.livestreamusingmvvm.ui.screen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.livestreamusingmvvm.R
import com.example.livestreamusingmvvm.ui.intent.LiveStreamsIntent
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
        // Retrieve the streamId from the Intent
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

        val state by viewModel.state.collectAsState()

        var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

//        LaunchedEffect(Unit) {
//            viewModel.handleIntent(LiveStreamsIntent.FetchLiveStreams)
//        }

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
                    BottomNavItem.Home -> HomeScreen()
                    BottomNavItem.LiveNow -> LiveNowScreen()
                    BottomNavItem.Profile -> DummyProfileScreen()
                    BottomNavItem.AllLiveShows -> {
                        state.liveStreams?.let {
                            viewModel.handleIntent(LiveStreamsIntent.RefreshLiveStreams())
                            AllLiveShowsScreen(
                                it,
                                isLoading = state.isLoading,
                                onRefresh = {
                                    viewModel.handleIntent(LiveStreamsIntent.RefreshLiveStreams())
                                }
                            )
                        }
                    }
                }

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        color = Color.Red,
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // Two items per row
                        modifier = Modifier.fillMaxSize()
                    ) {
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

@Composable
fun DummyProfileScreen() {
    // Profile data (can be replaced with dynamic data)
    val profileName = "Sumit Rai"
    val profileUsername = "@sumitrai"

    // Dummy profile picture (using default icon)
    val profilePicture: ImageVector = Icons.Default.AccountCircle

    // Dummy additional data
    val totalPosts = 256
    val totalFollowers = 1050
    val totalFollowing = 320
    val totalLove = 250
    val totalSupporters = 56

    // Dummy list of user posts (using default icons as placeholders)
    val posts = listOf(
        Icons.Default.AccountCircle,  // Placeholder icon
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle,
        Icons.Default.AccountCircle
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Profile Picture and Name Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Profile picture (using a circular image)
                Icon(
                    imageVector = profilePicture,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(16.dp)
                        .clip(CircleShape)
                )

                // Profile Name and Username
                Text(
                    text = profileName,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
                Text(
                    text = profileUsername,
                    style = TextStyle(
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )

                // Edit Profile Button
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { /* Handle Edit Profile */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile")
                }

                // Stats Section (Posts, Followers, Following)
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat(label = "Posts", value = totalPosts)
                    ProfileStat(label = "Followers", value = totalFollowers)
                    ProfileStat(label = "Following", value = totalFollowing)
                }

                // Additional dummy data (Love, Supporters)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Love: $totalLove",
                    style = TextStyle(
                        color = Color.Green,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total Supporters: $totalSupporters",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
        }

        // Scrollable User Posts Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "All Posts",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }

        // Horizontal Scrollable Grid of User Posts
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)  // Small padding between posts
                            .aspectRatio(9f / 16f)  // Set the aspect ratio to 9:16
                            .clip(RoundedCornerShape(8.dp))  // Rounded corners
                            .background(Color.Gray.copy(alpha = 0.2f))
                            .size(180.dp) // You can adjust the width and height as needed
                    ) {
                        Icon(
                            imageVector = posts[index],
                            contentDescription = "User Post",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TextStyle(
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDummyProfileScreen() {
    DummyProfileScreen()
}




