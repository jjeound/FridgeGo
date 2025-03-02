package com.example.untitled_capstone.presentation.feature.refrigerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.R
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.presentation.feature.refrigerator.event.FridgeAction
import com.example.untitled_capstone.presentation.feature.refrigerator.state.FridgeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FridgeViewModel: ViewModel() {
    private val _state = MutableStateFlow(FridgeState())
    val state: StateFlow<FridgeState> = _state.asStateFlow()

    init {
        onAction(FridgeAction.LoadItems)
    }

    fun onAction(action: FridgeAction){
        when(action){
            is FridgeAction.LoadItems -> loadItems()
            is FridgeAction.AddItem -> addItem(action.item)
            is FridgeAction.ToggleNotification -> toggleNotification(action.name)
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    fridgeItems =  listOf(
                        FridgeItem(
                            id = 1,
                            name = "name1",
                            image = R.drawable.ic_launcher_background,
                            quantity = "1",
                            expirationDate = 1739404800000,
                            notification = true,
                            isFridge = true
                        ),
                        FridgeItem(
                            id = 2,
                            name = "name2",
                            image = R.drawable.ic_launcher_background,
                            quantity = "2",
                            expirationDate = 1739404800000,
                            notification = true,
                            isFridge = true
                        ),
                        FridgeItem(
                            id = 3,
                            name = "name3",
                            image = null,
                            quantity = "3",
                            expirationDate = 1739404800000,
                            notification = false,
                            isFridge = true
                        ),
                        FridgeItem(
                            id = 4,
                            name = "name4",
                            image = null,
                            quantity = "4",
                            expirationDate = 1739404800000,
                            notification = false,
                            isFridge = true
                        ),
                        FridgeItem(
                            id = 5,
                            name = "냉동1",
                            image = R.drawable.ic_launcher_background,
                            quantity = "1",
                            expirationDate = 1739404800000,
                            notification = true,
                            isFridge = false
                        ),
                        FridgeItem(
                            id = 6,
                            name = "냉동2",
                            image = R.drawable.ic_launcher_background,
                            quantity = "2",
                            expirationDate = 1739404800000,
                            notification = true,
                            isFridge = false
                        ),
                        FridgeItem(
                            id = 7,
                            name = "냉삼",
                            image = null,
                            quantity = "3",
                            expirationDate = 1739404800000,
                            notification = false,
                            isFridge = false
                        ),
                        FridgeItem(
                            id = 8,
                            name = "냉장고를 부탁해",
                            image = null,
                            quantity = "4",
                            expirationDate = 1739404800000,
                            notification = false,
                            isFridge = false
                        )
                    ),
                    loading = false
                )
            }
        }
    }

    private fun addItem(item: FridgeItem){
        _state.update { currentState ->
            currentState.copy(fridgeItems = currentState.fridgeItems + item)
        }
        //call usecase or repository function to add data
    }

    private fun toggleNotification(name: String) {
        _state.update { currentState ->
            val updatedItems = currentState.fridgeItems.map { item ->
                if (item.name == name) {
                    item.copy(notification = !item.notification) // isFavorite 변경
                } else item
            }
            currentState.copy(fridgeItems = updatedItems)
        }
        //call usecase or repository function to add data
    }
}