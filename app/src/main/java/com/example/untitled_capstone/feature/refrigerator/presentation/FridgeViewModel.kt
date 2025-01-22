package com.example.untitled_capstone.feature.refrigerator.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.refrigerator.domain.model.FridgeItem
import com.example.untitled_capstone.feature.refrigerator.presentation.state.FridgeState

class FridgeViewModel: ViewModel() {
    var state by mutableStateOf(FridgeState())
    private set

    init {
        state = state.copy(
            fridgeItems = listOf(
                FridgeItem(
                    name = "name1",
                    image = R.drawable.ic_launcher_background,
                    quantity = 1,
                    expirationDate = "2021-12-31",
                    notification = true
                ),
                FridgeItem(
                    name = "name2",
                    image = R.drawable.ic_launcher_background,
                    quantity = 2,
                    expirationDate = "2021-12-31",
                    notification = true
                ),
                FridgeItem(
                    name = "name3",
                    image = null,
                    quantity = 3,
                    expirationDate = "2021-12-31",
                    notification = true
                ),
                FridgeItem(
                    name = "name4",
                    image = null,
                    quantity = 4,
                    expirationDate = "2021-12-31",
                    notification = true
                )
            ),
            loading = false
        )
    }
}