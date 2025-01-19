package com.example.untitled_capstone.feature.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    var selectedIndex by mutableIntStateOf(0)
        private set

    fun updateSelectedIndex(index: Int){
        selectedIndex = index
    }
}