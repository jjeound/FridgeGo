package com.example.untitled_capstone.presentation.feature.fridge.crud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.AddFridgeItemUseCase
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemByIdUseCase
import com.example.untitled_capstone.domain.use_case.fridge.ModifyFridgeItemUseCase
import com.example.untitled_capstone.presentation.util.UiEvent
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

    fun addItem(item: FridgeItem, image: File?) {
        viewModelScope.launch {
            addFridgeItemUseCase(item, image).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            uiState.tryEmit(FridgeCRUDUiState.Success)
                            //_event.emit(FridgeEvent.PopBackStack)
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

    fun modifyItem(item: FridgeItem, image: File?, isOriginalImageDeleted: Boolean) {
        viewModelScope.launch {
            modifyFridgeItemUseCase(item, image, isOriginalImageDeleted).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            uiState.tryEmit(FridgeCRUDUiState.Success)
                            //_event.emit(FridgeEvent.PopBackStack)
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