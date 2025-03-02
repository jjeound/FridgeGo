package com.example.untitled_capstone.presentation.feature.refrigerator.state

import com.example.untitled_capstone.domain.model.FridgeItem

data class FridgeState(
    val fridgeItems: List<FridgeItem> = emptyList(),
    val loading: Boolean = false
)
