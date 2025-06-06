package com.stone.fridge.presentation.feature.fridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.util.FridgeFetchType
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.use_case.fridge.DeleteFridgeItemUseCase
import com.stone.fridge.domain.use_case.fridge.GetFridgeItemsByDateUseCase
import com.stone.fridge.domain.use_case.fridge.GetFridgeItemsUseCase
import com.stone.fridge.domain.use_case.fridge.ToggleNotificationUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val toggleNotificationUseCase: ToggleNotificationUseCase,
    private val deleteFridgeItemUseCase: DeleteFridgeItemUseCase,
    private val getFridgeItemsUseCase: GetFridgeItemsUseCase,
    private val getFridgeItemsByDateUseCase: GetFridgeItemsByDateUseCase
): ViewModel() {

    val uiState: MutableStateFlow<FridgeUiState> = MutableStateFlow<FridgeUiState>(FridgeUiState.Idle)

    private val _fridgeItemPaged: MutableStateFlow<PagingData<FridgeItem>> = MutableStateFlow(PagingData.empty())
    val fridgeItemPaged = _fridgeItemPaged.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun toggleNotification(id: Long, alarmStatus: Boolean) {
        viewModelScope.launch {
            toggleNotificationUseCase(id, !alarmStatus).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { item ->
                            _fridgeItemPaged.update { pagingData ->
                                pagingData.map {
                                    if(it.id == id){
                                        it.copy(notification = !alarmStatus)
                                    }else{
                                        it
                                    }
                                }
                            }
                        }
                        uiState.tryEmit(FridgeUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(FridgeUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(FridgeUiState.Loading)
                    }
                }
            }
        }
    }



    fun deleteItem(id: Long) {
        viewModelScope.launch {
            deleteFridgeItemUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            _fridgeItemPaged.update { pagingData ->
                                pagingData.filter { it.id != id }
                            }
                        }
                        uiState.tryEmit(FridgeUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(FridgeUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(FridgeUiState.Loading)
                    }
                }
            }
        }
    }



    fun getItems() {
        viewModelScope.launch {
            getFridgeItemsUseCase(FridgeFetchType.OrderByCreated)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _fridgeItemPaged.value = pagingData
                }
        }
    }

    fun getItemsByDate() {
        viewModelScope.launch {
            getFridgeItemsByDateUseCase(FridgeFetchType.OrderByDate)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _fridgeItemPaged.value = pagingData
                }
        }
    }
}

interface FridgeUiState {
    data object Idle : FridgeUiState
    data object Loading : FridgeUiState
    data class Error(val message: String?) : FridgeUiState
}