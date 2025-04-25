package com.example.livestreamusingmvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.livestreamusingmvvm.R
import com.example.livestreamusingmvvm.databinding.FragmentLiveStreamsListBinding
import com.example.livestreamusingmvvm.remote.NetworkModule
import com.example.livestreamusingmvvm.repo.LiveStreamRepository
import com.example.livestreamusingmvvm.ui.adapter.LiveStreamAdapter
import com.example.livestreamusingmvvm.viewmodel.LiveStreamViewModel
import com.example.livestreamusingmvvm.viewmodel.viewmodelfactory.LiveStreamViewModelFactory

class LiveStreamsListFragment : Fragment() {

    private lateinit var liveStreamViewModel: LiveStreamViewModel
    private lateinit var liveStreamAdapter: LiveStreamAdapter
    private var _binding: FragmentLiveStreamsListBinding? = null
    private val binding get() = _binding!!

    // Assuming NetworkModule is available for API service
    private lateinit var liveStreamRepository: LiveStreamRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize ViewBinding
        _binding = FragmentLiveStreamsListBinding.inflate(inflater, container, false)

        // Initialize the repository with AntMediaApiService instance
        liveStreamRepository = LiveStreamRepository(NetworkModule.apiService)
        val factory = LiveStreamViewModelFactory(liveStreamRepository)
        liveStreamViewModel = ViewModelProvider(this, factory).get(LiveStreamViewModel::class.java)

        // Set up RecyclerView with the adapter
        liveStreamAdapter = LiveStreamAdapter(requireContext()) { stream ->
            val streamId = stream.streamId

            // Create a new instance of the LiveStreamPlayerFragment
            val fragment = LiveStreamPlayerFragment()

            // Bundle the arguments (streamId) to pass data to the new fragment
            val bundle = Bundle().apply {
                putString("streamId", streamId)
            }
            fragment.arguments = bundle

            // Open the new fragment (replace the current one)
            parentFragmentManager.beginTransaction()
                .replace(R.id.navhostfragment, fragment)  // Replace with the container ID
                .addToBackStack(null)  // Add to back stack to enable back navigation
                .commit()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = liveStreamAdapter

        // Observe ViewModel data for live streams
        liveStreamViewModel.liveStreams.observe(viewLifecycleOwner, Observer {
            liveStreamAdapter.submitList(it)
        })

        // Observe error state
        liveStreamViewModel.errorState.observe(viewLifecycleOwner, Observer { error ->
            showError(error)
        })

        // Observe loading state
        liveStreamViewModel.loadingState.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Fetch live streams when the fragment is created
        liveStreamViewModel.fetchLiveStreams()

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        // Show or hide loading spinner, for example:
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(error: String) {
        // Display error message in your UI, for example:
        binding.errorTextView.text = error
        binding.errorTextView.visibility = if (error.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }
}


