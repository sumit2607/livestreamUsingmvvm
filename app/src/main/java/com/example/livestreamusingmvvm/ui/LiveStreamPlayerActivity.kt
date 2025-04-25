package com.example.livestreamusingmvvm.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.livestreamusingmvvm.R
import com.example.livestreamusingmvvm.databinding.ActivityLiveStreamPlayerBinding
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class LiveStreamPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveStreamPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLiveStreamPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get streamId from Intent
        val streamId = intent.getStringExtra("streamId") ?: ""
        if (streamId.isNotEmpty()) {
            // Fetch live stream details for the given streamId
            fetchLiveStreamDetails(streamId)
        } else {
            showError("Stream ID is missing!")
        }
    }

    private fun fetchLiveStreamDetails(streamId: String) {
        // Show loading state
        showLoading(true)

        // Simulate a network call or data retrieval (e.g., from a local source or API)
        // Replace this with actual API call or data fetch logic
        // Here, we mock the response with hardcoded values
        Thread {
            try {
                // Simulate network delay or data fetch
                Thread.sleep(1000)

                // Mock data for this example
                val streamUrl = "https://antmedia.workuplift.com:5443/WebRTCAppEE"
                val streamRole = "Live Stream Role"
                val streamType = "Live"

                // Update UI on main thread
                runOnUiThread {
                    binding.streamTitle.text = streamRole
                    binding.streamStatus.text = streamType
                    setupPlayer(streamUrl)
                    showLoading(false)
                }
            } catch (e: Exception) {
                // Handle network or data fetching errors
                runOnUiThread {
                    showLoading(false)
                    showError("Error fetching stream details: ${e.message}")
                }
            }
        }.start()
    }

    private fun setupPlayer(streamUrl: String) {
        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build().apply {
            val mediaItem = MediaItem.fromUri(streamUrl)
            setMediaItem(mediaItem)
            prepare()
            play()
        }

        // Bind the player to the PlayerView in your layout
        binding.playerView.player = player as com.google.android.exoplayer2.Player?
    }

    private fun showLoading(isLoading: Boolean) {
        // Show or hide loading spinner based on loading state
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(error: String) {
        // Display error message (Toast)
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        // Optionally, show error in the UI
        binding.errorMessage.text = error
        binding.errorMessage.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        // Release the player when the activity is stopped
        player?.release()
    }
}
