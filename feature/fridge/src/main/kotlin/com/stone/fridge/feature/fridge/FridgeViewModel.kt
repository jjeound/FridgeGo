package com.stone.fridge.feature.fridge

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.stone.fridge.core.data.fridge.FridgeRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.paging.FridgeFetchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val fridgeRepository: FridgeRepository
): ViewModel() {

    internal val uiState: MutableStateFlow<FridgeUiState> = MutableStateFlow(FridgeUiState.Idle)

    private val _fridgeItemPaged: MutableStateFlow<PagingData<Fridge>> = MutableStateFlow(PagingData.empty())
    val fridgeItemPaged = _fridgeItemPaged.asStateFlow()

    private val _freezerItemPaged: MutableStateFlow<PagingData<Fridge>> = MutableStateFlow(PagingData.empty())
    val freezerItemPaged = _freezerItemPaged.asStateFlow()

    private val _topSelector: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val topSelector = _topSelector.asStateFlow()

    init {
        getItems(FridgeFetchType.OrderByCreated)
    }

    fun updateTopSelector() {
        _topSelector.value = !_topSelector.value
    }

    fun toggleNotification(id: Long, alarmStatus: Boolean) {
        viewModelScope.launch {
            fridgeRepository.toggleNotification(id, !alarmStatus)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Loading -> uiState.emit(FridgeUiState.Loading)
                        is Resource.Success -> {
                            _fridgeItemPaged.update { pagingData ->
                                pagingData.map {
                                    if(it.id == id){
                                        it.copy(alarmStatus = !alarmStatus)
                                    }else{
                                        it
                                    }
                                }
                            }
                            uiState.emit(FridgeUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(FridgeUiState.Error(result.message))
                    }
                }
        }
    }



    fun deleteItem(id: Long) {
        viewModelScope.launch {
            fridgeRepository.deleteItem(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Loading -> uiState.emit(FridgeUiState.Loading)
                        is Resource.Success -> {
                            _fridgeItemPaged.update { pagingData ->
                                pagingData.filter { it.id != id }
                            }
                            uiState.emit(FridgeUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(FridgeUiState.Error(result.message))
                    }
                }
        }
    }



    fun getItems(fetchType: FridgeFetchType) {
        viewModelScope.launch {
            fridgeRepository.getFridgeItems(fetchType)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _fridgeItemPaged.value = pagingData.filter { it.storageType }
                    _freezerItemPaged.value = pagingData.filter { !it.storageType }
                }
        }
    }
}

@Stable
internal sealed interface FridgeUiState {
    data object Idle : FridgeUiState
    data object Loading : FridgeUiState
    data class Error(val message: String) : FridgeUiState
}