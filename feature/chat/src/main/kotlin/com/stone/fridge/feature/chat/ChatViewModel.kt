package com.stone.fridge.feature.chat

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.chat.ChatRepository
import com.stone.fridge.core.model.ChatRoomRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
): ViewModel() {

    val uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)

    val chattingRooms: StateFlow<List<ChatRoomRaw>> = chatRepository.getMyRooms()
        .onCompletion {
            uiState.value = ChatUiState.Idle
        }
        .catch {
            uiState.value = ChatUiState.Error(it.message ?: "알 수 없는 오류가 발생했어요.")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

@Stable
sealed interface ChatUiState {
    data object Idle : ChatUiState
    data object Loading : ChatUiState
    data class Error(val message: String) : ChatUiState
}