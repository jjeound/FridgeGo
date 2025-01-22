package com.example.untitled_capstone.feature.refrigerator.presentation.state

import com.example.untitled_capstone.feature.refrigerator.domain.model.FridgeItem

data class FridgeState(
    val fridgeItems: List<FridgeItem> = emptyList(),
    val loading: Boolean = false
)
