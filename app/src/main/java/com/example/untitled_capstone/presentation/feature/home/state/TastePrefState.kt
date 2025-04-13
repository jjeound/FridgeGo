package com.example.untitled_capstone.presentation.feature.home.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TastePrefState{
    var data by mutableStateOf("")
    var isLoading by mutableStateOf(false)
}