package com.example.untitled_capstone.presentation.feature.fridge

import com.example.untitled_capstone.domain.model.FridgeItem
import java.io.File

sealed interface FridgeAction{
    data object GetItems: FridgeAction
    data object GetItemsByDate: FridgeAction
    data class AddItem(val item: FridgeItem, val image: File?): FridgeAction
    data class ToggleNotification(val id: Long, val alarmStatus: Boolean): FridgeAction
    data class ModifyItem(val item: FridgeItem): FridgeAction
    data class DeleteItem(val id: Long): FridgeAction
    data class GetItemById(val id: Long): FridgeAction
    data object InitState: FridgeAction
}