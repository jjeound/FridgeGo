package com.example.untitled_capstone.feature.refrigerator.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.refrigerator.domain.model.FridgeItem
import com.example.untitled_capstone.feature.refrigerator.presentation.event.FridgeAction
import com.example.untitled_capstone.feature.refrigerator.presentation.state.FridgeState

class FridgeViewModel: ViewModel() {
    var state by mutableStateOf(FridgeState())
    private set

    init {
        state = state.copy(
            fridgeItems = listOf(
                FridgeItem(
                    id = 1,
                    name = "name1",
                    image = R.drawable.ic_launcher_background,
                    quantity = 1,
                    expirationDate = 1739404800000,
                    notification = true,
                    isFridge = true
                ),
                FridgeItem(
                    id = 2,
                    name = "name2",
                    image = R.drawable.ic_launcher_background,
                    quantity = 2,
                    expirationDate = 1739404800000,
                    notification = true,
                    isFridge = true
                ),
                FridgeItem(
                    id = 3,
                    name = "name3",
                    image = null,
                    quantity = 3,
                    expirationDate = 1739404800000,
                    notification = false,
                    isFridge = true
                ),
                FridgeItem(
                    id = 4,
                    name = "name4",
                    image = null,
                    quantity = 4,
                    expirationDate = 1739404800000,
                    notification = false,
                    isFridge = true
                ),
                FridgeItem(
                    id = 5,
                    name = "냉동1",
                    image = R.drawable.ic_launcher_background,
                    quantity = 1,
                    expirationDate = 1739404800000,
                    notification = true,
                    isFridge = false
                ),
                FridgeItem(
                    id = 6,
                    name = "냉동2",
                    image = R.drawable.ic_launcher_background,
                    quantity = 2,
                    expirationDate = 1739404800000,
                    notification = true,
                    isFridge = false
                ),
                FridgeItem(
                    id = 7,
                    name = "냉삼",
                    image = null,
                    quantity = 3,
                    expirationDate = 1739404800000,
                    notification = false,
                    isFridge = false
                ),
                FridgeItem(
                    id = 8,
                    name = "냉장고를 부탁해",
                    image = null,
                    quantity = 4,
                    expirationDate = 1739404800000,
                    notification = false,
                    isFridge = false
                )
            ),
            loading = false
        )
    }

    fun toggleNotification(fridgeItem: FridgeItem) {
        val updatedItems = state.fridgeItems.map { item ->
            if (item.name == fridgeItem.name) {
                item.copy(notification = !item.notification)
            } else item
        }
        state = state.copy(fridgeItems = updatedItems)
    }
}