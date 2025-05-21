package com.example.untitled_capstone.presentation.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.use_case.chat.GetMyChatRoomsUseCase
import com.example.untitled_capstone.navigation.Screen
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

    private val _event = MutableSharedFlow<ChatEvent>()
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
                        _event.emit(ChatEvent.ShowSnackbar(it.message ?: "Unknown error"))
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

interface ChatEvent{
    data class ShowSnackbar(val message: String) : ChatEvent
    data class Navigate(val route: Screen) : ChatEvent
    object PopBackStack : ChatEvent
    object ClearBackStack : ChatEvent
}