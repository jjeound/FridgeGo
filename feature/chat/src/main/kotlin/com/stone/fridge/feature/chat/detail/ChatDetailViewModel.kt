package com.stone.fridge.feature.chat.detail

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stone.fridge.core.data.chat.ChatRepository
import com.stone.fridge.core.data.local.LocalUserRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.domain.EnterAndConnectChatRoomUseCase
import com.stone.fridge.core.model.ChatMember
import com.stone.fridge.core.model.ChatRoom
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.model.UnreadBroadcast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val enterAndConnectChatRoomUseCase: EnterAndConnectChatRoomUseCase,
    private val localUserRepository: LocalUserRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    internal val uiState: MutableStateFlow<ChatDetailUiState> = MutableStateFlow(ChatDetailUiState.Loading)

    val roomId = savedStateHandle.getStateFlow<Long?>("roomId", null)

    private val _chattingRoom = MutableStateFlow<ChatRoom?>(null)
    val chattingRoom = _chattingRoom.asStateFlow()

    private val _message: MutableStateFlow<PagingData<Message>> = MutableStateFlow(PagingData.empty())
    val message = _message.asStateFlow()

    private val _member = MutableStateFlow<List<ChatMember>>(emptyList())
    val member = _member.asStateFlow()

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    init {
        roomId
            .filterNotNull()
            .flatMapLatest { id ->
                enterAndConnectChatRoomUseCase(
                    id, {
                        subscribe(id)
                        readChats(id)
                    }, {
                        Log.d("webSocket", "Error connecting to socket: ${it.message}")
                    }
                ).onEach {
                    _chattingRoom.value = it
                }.map { id }
            }.flatMapLatest { id ->
                chatRepository.getMessagePaged(id)
                    .cachedIn(viewModelScope)
                    .onEach { messages ->
                        _message.value = messages
                    }
            }
            .onCompletion { uiState.value = ChatDetailUiState.Idle }
            .catch {
                uiState.value = ChatDetailUiState.Error(it.message ?: "알 수 없는 오류가 발생했어요.")
            }.launchIn(viewModelScope)
        getUserId()
    }

    fun readChats(id: Long) {
        viewModelScope.launch {
            chatRepository.readChats(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ChatDetailUiState.Idle)
                        is Resource.Error -> uiState.emit(ChatDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ChatDetailUiState.Loading)
                    }
            }
        }
    }

    fun getMyName(){
        viewModelScope.launch {
            _name.value = localUserRepository.getNickname() ?: "USER"
        }
    }

    fun getUserId() {
        viewModelScope.launch {
            _userId.value = localUserRepository.getUserId()
        }
    }

    fun checkWhoIsIn(id: Long) {
        viewModelScope.launch {
            chatRepository.checkWhoIsIn(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ChatDetailUiState.Idle)
                        is Resource.Error -> uiState.emit(ChatDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ChatDetailUiState.Loading)
                    }
                }
        }
    }

    fun closeChatRoom(id: Long) {
        viewModelScope.launch {
            chatRepository.closeChatRoom(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ChatDetailUiState.Success)
                        is Resource.Error -> uiState.emit(ChatDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ChatDetailUiState.Loading)
                    }
                }
        }
    }

    fun exitChatRoom(id: Long) {
        viewModelScope.launch {
            chatRepository.exitChatRoom(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ChatDetailUiState.Success)
                        is Resource.Error -> uiState.emit(ChatDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ChatDetailUiState.Loading)
                    }
                }
        }
    }

    private fun subscribe(roomId: Long) {
        viewModelScope.launch {
            chatRepository.subscribeRoom(
                roomId,
                onUnreadUpdate = { unread ->
                    updateUnreadCount(unread)
                }
            )
        }
    }

    private fun updateUnreadCount(unread: UnreadBroadcast) {
        viewModelScope.launch {
            val currentMessages = _message.value

            val updated = currentMessages.map { message ->
                if (message.messageId == unread.messageId) {
                    message.copy(unreadCount = unread.unreadCount)
                } else message
            }

            _message.value = updated
        }
    }

    fun sendMessage(roomId: Long, content: String) {
        chatRepository.sendMessage(roomId, content)
    }

    fun sendRead(roomId: Long) {
        chatRepository.sendReadEvent(roomId)
    }

    fun leaveRoom(roomId: Long){
        chatRepository.leaveRoom(roomId)
    }
}

@Stable
internal sealed interface ChatDetailUiState {
    data object Idle : ChatDetailUiState
    data object Loading : ChatDetailUiState
    data object Success : ChatDetailUiState
    data class Error(val message: String) : ChatDetailUiState
}