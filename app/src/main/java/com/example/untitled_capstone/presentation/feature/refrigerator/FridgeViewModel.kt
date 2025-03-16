package com.example.untitled_capstone.presentation.feature.refrigerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.untitled_capstone.R
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.presentation.feature.refrigerator.event.FridgeAction
import com.example.untitled_capstone.presentation.feature.refrigerator.state.FridgeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class FridgeViewModel @Inject constructor(
    private val fridgeUseCases: FridgeUseCases
): ViewModel() {
    private val _state = MutableStateFlow(FridgeState(loading = true))
    val state: StateFlow<FridgeState> = _state.asStateFlow()

    init {
        onAction(FridgeAction.GetItems)
    }

    fun onAction(action: FridgeAction){
        when(action){
            is FridgeAction.GetItems -> getItems()
            is FridgeAction.AddItem -> addItem(action.item)
            is FridgeAction.ToggleNotification -> toggleNotification(action.id)
            is FridgeAction.ModifyItem -> modifyItem(action.item)
            is FridgeAction.DeleteItem -> deleteItem(action.id)
        }
    }

    private fun addItem(item: FridgeItem){
        viewModelScope.launch {
            fridgeUseCases.addFridgeItem(item)
        }
//        _state.update { currentState ->
//            currentState.copy(fridgeItems = currentState.fridgeItems + item)
//        }
        //call usecase or repository function to add data
    }

    private fun toggleNotification(id: Int) {
        viewModelScope.launch {
            fridgeUseCases.toggleNotification(id)
        }
//        _state.update { currentState ->
//            val updatedItems = currentState.fridgeItems.map { item ->
//                if (item.id == id) {
//                    item.copy(notification = !item.notification) // isFavorite 변경
//                } else item
//            }
//            currentState.copy(fridgeItems = updatedItems)
//        }
        //call usecase or repository function to add data
    }

    private fun modifyItem(updatedItem: FridgeItem) {
        viewModelScope.launch {
            fridgeUseCases.modifyFridgeItems(updatedItem)
        }
//        _state.update { currentState ->
//            currentState.copy(
//                fridgeItems = currentState.fridgeItems.map { item ->
//                    if (item.id == updatedItem.id) updatedItem else item
//                }
//            )
//        }
    }

    private fun deleteItem(id: Int) {
        viewModelScope.launch {
            fridgeUseCases.deleteFridgeItem(id)
        }
//        _state.update { currentState ->
//            currentState.copy(fridgeItems = currentState.fridgeItems.filterNot { it.id == id })
//        }
    }

    private fun getItems() {
        _state.update {
            it.copy(loading = true)
        }
        val fridgeItems = fridgeUseCases.getFridgeItem().cachedIn(viewModelScope)
        _state.update {
            it.copy(fridgeItems = fridgeItems, loading = false)
        }
//        viewModelScope.launch {
//            _state.update {
//                it.copy(loading = false)
//                it.copy(
//                    fridgeItems =  listOf(
//                        FridgeItem(
//                            id = 1,
//                            name = "name1",
//                            image = null,
//                            quantity = "1",
//                            expirationDate = 1739404800000,
//                            notification = true,
//                            isFridge = true
//                        ),
//                        FridgeItem(
//                            id = 2,
//                            name = "name2",
//                            image = null,
//                            quantity = "2",
//                            expirationDate = 1739404800000,
//                            notification = true,
//                            isFridge = true
//                        ),
//                        FridgeItem(
//                            id = 3,
//                            name = "name3",
//                            image = null,
//                            quantity = "3",
//                            expirationDate = 1739404800000,
//                            notification = false,
//                            isFridge = true
//                        ),
//                        FridgeItem(
//                            id = 4,
//                            name = "name4",
//                            image = null,
//                            quantity = "4",
//                            expirationDate = 1739404800000,
//                            notification = false,
//                            isFridge = true
//                        ),
//                        FridgeItem(
//                            id = 5,
//                            name = "냉동1",
//                            image = null,
//                            quantity = "1",
//                            expirationDate = 1739404800000,
//                            notification = true,
//                            isFridge = false
//                        ),
//                        FridgeItem(
//                            id = 6,
//                            name = "냉동2",
//                            image = null,
//                            quantity = "2",
//                            expirationDate = 1739404800000,
//                            notification = true,
//                            isFridge = false
//                        ),
//                        FridgeItem(
//                            id = 7,
//                            name = "냉삼",
//                            image = null,
//                            quantity = "3",
//                            expirationDate = 1739404800000,
//                            notification = false,
//                            isFridge = false
//                        ),
//                        FridgeItem(
//                            id = 8,
//                            name = "냉장고를 부탁해",
//                            image = null,
//                            quantity = "4",
//                            expirationDate = 1739404800000,
//                            notification = false,
//                            isFridge = false
//                        )
//                    ),
//                    loading = false
//                )
//            }
//        }
    }
}