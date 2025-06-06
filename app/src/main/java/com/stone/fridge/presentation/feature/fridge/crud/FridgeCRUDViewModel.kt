package com.stone.fridge.presentation.feature.fridge.crud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.model.NewFridge
import com.stone.fridge.domain.use_case.fridge.AddFridgeItemUseCase
import com.stone.fridge.domain.use_case.fridge.GetFridgeItemByIdUseCase
import com.stone.fridge.domain.use_case.fridge.ModifyFridgeItemUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FridgeCRUDViewModel @Inject constructor(
    private val addFridgeItemUseCase: AddFridgeItemUseCase,
    private val modifyFridgeItemUseCase: ModifyFridgeItemUseCase,
    private val getFridgeItemByIdUseCase: GetFridgeItemByIdUseCase,
): ViewModel() {
    val uiState: MutableStateFlow<FridgeCRUDUiState> = MutableStateFlow<FridgeCRUDUiState>(FridgeCRUDUiState.Idle)

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _fridgeItem = MutableStateFlow<FridgeItem?>(null)
    val fridgeItem = _fridgeItem.asStateFlow()

    fun getItemById(id: Long) {
        viewModelScope.launch {
            getFridgeItemByIdUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { item ->
                            _fridgeItem.value = item
                            uiState.tryEmit(FridgeCRUDUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(FridgeCRUDUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(FridgeCRUDUiState.Loading)
                    }
                }
            }
        }
    }

    fun addItem(item: NewFridge, image: File?) {
        viewModelScope.launch {
            addFridgeItemUseCase(item, image).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            uiState.tryEmit(FridgeCRUDUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "알 수 없는 오류가 발생했습니다."))
                        uiState.tryEmit(FridgeCRUDUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(FridgeCRUDUiState.Loading)
                    }
                }
            }
        }
    }

    fun modifyItem(item: FridgeItem, image: File?) {
        viewModelScope.launch {
            modifyFridgeItemUseCase(item, image).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            uiState.tryEmit(FridgeCRUDUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(FridgeCRUDUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(FridgeCRUDUiState.Loading)
                    }
                }
            }
        }
    }
}

interface FridgeCRUDUiState {
    data object Idle : FridgeCRUDUiState
    data object Success: FridgeCRUDUiState
    data object Loading : FridgeCRUDUiState
    data class Error(val message: String?) : FridgeCRUDUiState
}