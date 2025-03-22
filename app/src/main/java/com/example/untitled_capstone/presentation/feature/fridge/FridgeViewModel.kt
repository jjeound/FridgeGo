package com.example.untitled_capstone.presentation.feature.fridge

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.presentation.feature.fridge.FridgeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val fridgeUseCases: FridgeUseCases
): ViewModel() {
    private val _state = MutableStateFlow(FridgeState())
    val state = _state.asStateFlow()

    init {
        onAction(FridgeAction.GetItems)
    }

    fun onAction(action: FridgeAction){
        when(action){
            is FridgeAction.GetItems -> getItems()
            is FridgeAction.AddItem -> addItem(action.item)
            is FridgeAction.GetItemsByDate -> getItemsByDate()
            is FridgeAction.ToggleNotification -> toggleNotification(action.id, action.alarmStatus)
            is FridgeAction.ModifyItem -> modifyItem(action.item)
            is FridgeAction.DeleteItem -> deleteItem(action.id)
        }
    }

    private fun addItem(item: FridgeItem){
        viewModelScope.launch {
            val result = fridgeUseCases.addFridgeItem(item)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                    Log.d("result", result.data.toString())
                    fridgeUseCases.invalidatePagingSource()
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun toggleNotification(id: Long, alarmStatus: Boolean) {
        viewModelScope.launch {
            val result = fridgeUseCases.toggleNotification(id, alarmStatus)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                    fridgeUseCases.invalidatePagingSource()
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun modifyItem(updatedItem: FridgeItem) {
        viewModelScope.launch {
            val result = fridgeUseCases.modifyFridgeItems(updatedItem)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                    fridgeUseCases.invalidatePagingSource()
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun deleteItem(id: Long) {
        viewModelScope.launch {
            val result = fridgeUseCases.deleteFridgeItem(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                    fridgeUseCases.invalidatePagingSource()
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun getItems() {
        viewModelScope.launch {
            val fridgeItems = fridgeUseCases.getFridgeItems()
                .cachedIn(viewModelScope)
            _state.value =  _state.value.copy(fridgeItems = fridgeItems, loading = false)
        }
    }

    private fun getItemsByDate() {
        viewModelScope.launch {
            val fridgeItems = fridgeUseCases.getFridgeItemsByDate()
                .cachedIn(viewModelScope)
            _state.value =  _state.value.copy(fridgeItems = fridgeItems, loading = false)
        }
    }
}