package com.example.untitled_capstone.presentation.feature.fridge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.untitled_capstone.domain.model.FridgeItem

class FridgeState{
    var response by mutableStateOf<FridgeItem?>(null)
    var isLoading by mutableStateOf(false)
}