package com.example.untitled_capstone.presentation.feature.fridge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val fridgeUseCases: FridgeUseCases
): ViewModel() {

    var state by mutableStateOf(FridgeState())
        private set

    private val _fridgeItemState: MutableStateFlow<PagingData<FridgeItem>> = MutableStateFlow(PagingData.empty())
    val fridgeItemState = _fridgeItemState.asStateFlow()

    private val _event = MutableSharedFlow<UIEvent>()
    val event = _event.asSharedFlow()

    init {
        getItems()
    }

    fun onAction(action: FridgeAction){
        when(action){
            is FridgeAction.GetItems -> getItems()
            is FridgeAction.AddItem -> addItem(action.item, action.image)
            is FridgeAction.GetItemsByDate -> getItemsByDate()
            is FridgeAction.ToggleNotification -> toggleNotification(action.id, action.alarmStatus)
            is FridgeAction.ModifyItem -> modifyItem(action.item)
            is FridgeAction.DeleteItem -> deleteItem(action.id)
            is FridgeAction.GetItemById -> getItemById(action.id)
            is FridgeAction.InitState -> initState()
        }
    }

    private fun initState(){
        state = FridgeState()
    }

    private fun addItem(item: FridgeItem, image: File?) {
        viewModelScope.launch {
            val result = fridgeUseCases.addFridgeItem(item, image)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        getItems()
                    }
                }
                is Resource.Error -> {
                    state.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    state.isLoading = true
                }
            }

        }
    }

    private fun toggleNotification(id: Long, alarmStatus: Boolean) {
        viewModelScope.launch {
            val result = fridgeUseCases.toggleNotification(id, !alarmStatus)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        state.isLoading = false
                        _fridgeItemState.update { pagingData ->
                            pagingData.map {
                                if(it.id == id){
                                    it.copy(notification = !alarmStatus)
                                }else{
                                    it
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    state.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    state.isLoading = true
                }
            }
        }
    }

    private fun modifyItem(item: FridgeItem) {
        viewModelScope.launch {
            val result = fridgeUseCases.modifyFridgeItems(item)
            when(result){
                is Resource.Success -> {
                    _fridgeItemState.update { pagingData->
                        pagingData.map {
                            if(it.id == item.id){
                                item
                            }else{
                                it
                            }
                        }
                    }
                    state.isLoading = false
                }
                is Resource.Error -> {
                    state.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    state.isLoading = true
                }
            }
        }
    }

    private fun deleteItem(id: Long) {
        viewModelScope.launch {
            val result = fridgeUseCases.deleteFridgeItem(id)
            when(result){
                is Resource.Success -> {
                    state.isLoading = false
                    _fridgeItemState.update { pagingData ->
                        pagingData.filter { it.id != id }
                    }
                }
                is Resource.Error -> {
                    state.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    state.isLoading = true
                }
            }
        }
    }

    private fun getItemById(id: Long) {
        viewModelScope.launch {
            val result = fridgeUseCases.getFridgeItemById(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        state.apply {
                            response = it
                            isLoading = false
                        }
                    }
                }
                is Resource.Error -> {
                    state.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    state.isLoading = true
                }
            }
        }
    }

    private fun getItems() {
        viewModelScope.launch {
            fridgeUseCases.getFridgeItems(FridgeFetchType.OrderByCreated)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _fridgeItemState.value = pagingData
                }
        }
    }

    private fun getItemsByDate() {
        viewModelScope.launch {
            fridgeUseCases.getFridgeItemsByDate(FridgeFetchType.OrderByDate)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _fridgeItemState.value = pagingData
                }
        }
    }

    fun navigateUp(route: Screen) {
        viewModelScope.launch {
            _event.emit(UIEvent.Navigate(route))
        }
    }

    fun popBackStack() {
        viewModelScope.launch {
            _event.emit(UIEvent.PopBackStack)
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _event.emit(UIEvent.ShowSnackbar(message))
        }
    }
}