package com.example.untitled_capstone.feature.refrigerator.presentation.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.feature.refrigerator.presentation.composable.FridgeItemContainer
import com.example.untitled_capstone.feature.refrigerator.presentation.state.FridgeState

@Composable
fun RefrigeratorScreen(state: FridgeState) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.padding(10.dp))
        }
        items(state.fridgeItems) { item ->
            FridgeItemContainer(item)
        }
    }
}