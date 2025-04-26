package com.example.livestreamusingmvvm

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livestreamusingmvvm.databinding.ActivityMainBinding
import com.example.livestreamusingmvvm.ui.LiveStreamPlayerFragment
import com.example.livestreamusingmvvm.ui.LiveStreamsListFragment
import com.example.livestreamusingmvvm.ui.PublishStreamActivity

class MainActivity : AppCompatActivity() {

    // Declare the ViewBinding variable
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewBinding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge layout (this method is assumed to be defined elsewhere in your code)
        enableEdgeToEdge()

        // Apply window insets to adjust padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Create a new instance of the LiveStreamPlayerFragment
        val fragment = LiveStreamsListFragment()

        // Open the new fragment (replace the current one)
        supportFragmentManager.beginTransaction()
            .replace(R.id.navhostfragment, fragment)  // Replace with the container ID
            .addToBackStack(null)  // Add to back stack to enable back navigation
            .commit()
//        val intent = Intent(this, PublishStreamActivity::class.java)
//        startActivity(intent)
    }
}
