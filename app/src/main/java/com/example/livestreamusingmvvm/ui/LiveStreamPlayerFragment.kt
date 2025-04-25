package com.example.livestreamusingmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamusingmvvm.databinding.FragmentLiveStreamPlayerBinding
import com.example.livestreamusingmvvm.viewmodel.LiveStreamViewModel

class LiveStreamPlayerFragment : Fragment() {

    private lateinit var liveStreamViewModel: LiveStreamViewModel
    private lateinit var binding: FragmentLiveStreamPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        binding = FragmentLiveStreamPlayerBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        liveStreamViewModel = ViewModelProvider(this).get(LiveStreamViewModel::class.java)

        // Retrieve the streamId from arguments
        val streamId = arguments?.getString("streamId") ?: ""

        // Fetch live stream data based on streamId
        liveStreamViewModel.fetchLiveStreamDetails(streamId)

        // Observe the live stream data and update UI
        liveStreamViewModel.liveStreamDetails.observe(viewLifecycleOwner, { stream ->
            binding.streamTitle.text = stream?.role
            binding.streamStatus.text = stream?.type
            // You can integrate an actual player here.
            // For example: setupPlayer(stream.streamUrl) to show the live stream.
        })

        // Handle loading state (show a loading spinner or progress bar)
        liveStreamViewModel.loadingState.observe(viewLifecycleOwner, { isLoading ->
            showLoading(isLoading)
        })

        // Handle error state (display an error message)
        liveStreamViewModel.errorState.observe(viewLifecycleOwner, { error ->
            showError(error)
        })

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        // Toggle the loading state (e.g., show a progress bar or spinner)
        if (isLoading) {
            binding.loadingSpinner.visibility = View.VISIBLE
        } else {
            binding.loadingSpinner.visibility = View.GONE
        }
    }

    private fun showError(error: String) {
        // Display error message (e.g., Toast or TextView)
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        // Optionally, show an error message in the UI
        binding.errorMessage.text = error
    }
}

