package com.example.untitled_capstone.presentation.feature.refrigerator.event

import com.example.untitled_capstone.domain.model.FridgeItem

sealed interface FridgeAction{
    data object GetItems: FridgeAction
    data class AddItem(val item: FridgeItem): FridgeAction
    data class ToggleNotification(val id: Int): FridgeAction
    data class ModifyItem(val item: FridgeItem): FridgeAction
    data class DeleteItem(val id: Int): FridgeAction
}