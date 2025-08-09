package com.stone.fridge.feature.fridge.crud

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.fridge.FridgeRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.model.ModifyFridgeReq
import com.stone.fridge.core.model.NewFridge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FridgeCRUDViewModel @Inject constructor(
    private val fridgeRepository: FridgeRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val uiState: MutableStateFlow<FridgeCRUDUiState> = MutableStateFlow(FridgeCRUDUiState.Idle)

    val id = savedStateHandle.getStateFlow<Long?>("id", null)

    val scannedDate = savedStateHandle.getStateFlow<String?>("date", null)

    val fridge: StateFlow<Fridge?> = id.filterNotNull().flatMapLatest { id ->
        fridgeRepository.getFridgeItemById(id)
    }.onStart { uiState.value = FridgeCRUDUiState.Loading }
        .onCompletion {
            uiState.value = FridgeCRUDUiState.Idle
        }
        .catch {
            uiState.value = FridgeCRUDUiState.Error(it.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun addItem(item: NewFridge, image: File?) {
        viewModelScope.launch {
            fridgeRepository.addItem(item, image)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(FridgeCRUDUiState.Success)
                        is Resource.Error -> uiState.emit(FridgeCRUDUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(FridgeCRUDUiState.Loading)
                    }
                }
        }
    }

    fun modifyItem(id: Long, item: ModifyFridgeReq, image: File?) {
        viewModelScope.launch {
            fridgeRepository.modifyItem(id,item, image)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(FridgeCRUDUiState.Success)
                        is Resource.Error -> uiState.emit(FridgeCRUDUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(FridgeCRUDUiState.Loading)
                    }
                }
        }
    }
}

@Stable
sealed interface FridgeCRUDUiState {
    data object Idle : FridgeCRUDUiState
    data object Success: FridgeCRUDUiState
    data object Loading : FridgeCRUDUiState
    data class Error(val message: String) : FridgeCRUDUiState
}