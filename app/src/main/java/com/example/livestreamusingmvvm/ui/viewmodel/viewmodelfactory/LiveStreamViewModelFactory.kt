package com.example.livestreamusingmvvm.ui.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.livestreamusingmvvm.repository.LiveStreamRepository
import com.example.livestreamusingmvvm.ui.viewmodel.LiveStreamViewModel

class LiveStreamViewModelFactory(
    private val repository: LiveStreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LiveStreamViewModel::class.java)) {
            return LiveStreamViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
