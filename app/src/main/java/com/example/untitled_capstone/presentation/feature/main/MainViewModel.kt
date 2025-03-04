package com.example.untitled_capstone.presentation.feature.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    var topSelector by mutableStateOf(true)
        private set

    fun updateTopSelector() {
        topSelector = !topSelector
    }
}