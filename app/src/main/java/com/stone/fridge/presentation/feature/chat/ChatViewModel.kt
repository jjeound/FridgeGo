package com.stone.fridge.presentation.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.ChattingRoomRaw
import com.stone.fridge.domain.use_case.chat.GetMyChatRoomsUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMyRoomsUseCase: GetMyChatRoomsUseCase
): ViewModel() {

    val uiState: MutableStateFlow<ChatUiState> = MutableStateFlow<ChatUiState>(ChatUiState.Loading)

    private val _chattingRoomList = MutableStateFlow<List<ChattingRoomRaw>>(emptyList())
    val chattingRoomList = _chattingRoomList.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        getMyRooms()
    }

    fun getMyRooms() {
        viewModelScope.launch {
            getMyRoomsUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { rooms ->
                            _chattingRoomList.update { rooms }
                            uiState.tryEmit(ChatUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatUiState.Loading)
                    }
                }
            }
        }
    }
}

interface ChatUiState {
    data object Idle : ChatUiState
    data object Loading : ChatUiState
    data class Error(val message: String?) : ChatUiState
}