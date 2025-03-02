package com.example.untitled_capstone.presentation.feature.refrigerator.event

import com.example.untitled_capstone.domain.model.FridgeItem

sealed interface FridgeAction{
    data object LoadItems: FridgeAction
    data class AddItem(val item: FridgeItem): FridgeAction
    data class ToggleNotification(val name: String): FridgeAction
}