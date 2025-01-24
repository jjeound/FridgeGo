package com.example.untitled_capstone.feature.refrigerator.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

@Serializable
object AddFridgeItemNav

@Composable
fun AddFridgeItemScreen(){
    var addItemState by remember { mutableStateOf(false) }
    Box {
        
    }
}