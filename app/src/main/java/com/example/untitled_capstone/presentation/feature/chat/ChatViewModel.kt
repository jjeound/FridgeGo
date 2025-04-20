package com.example.untitled_capstone.presentation.feature.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.model.UnreadBroadcast
import com.example.untitled_capstone.domain.use_case.chat.ChatUseCases
import com.example.untitled_capstone.domain.use_case.my.GetAccessToken
import com.example.untitled_capstone.domain.use_case.post.GetNickname
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.state.ChatUiState
import com.example.untitled_capstone.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val getNickname: GetNickname,
    private val getAccessToken: GetAccessToken
): ViewModel() {

    private val _state = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val state = _state.asStateFlow()

    var chattingRoomList by mutableStateOf<List<ChattingRoomRaw>>(emptyList())
        private set

    val chattingRoom = mutableStateOf<ChattingRoom?>(null)

    private val _message: MutableStateFlow<PagingData<Message>> = MutableStateFlow(PagingData.empty())
    val message = _message.asStateFlow()

    var member by mutableStateOf<List<ChatMember>>(emptyList())
        private set

    var name by mutableStateOf<String>("")
        private set

    private val _event = MutableSharedFlow<UIEvent>()
    val event = _event.asSharedFlow()

    fun readChats(id: Long) {
        viewModelScope.launch {
            val result = chatUseCases.readChats(id)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                    }
                }

                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun getMyRooms() {
        viewModelScope.launch {
            val result = chatUseCases.getMyRooms()
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                        chattingRoomList = it
                    }
                }

                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun exitChatRoom(id: Long) {
        viewModelScope.launch {
            val result = chatUseCases.exitChatRoom(id)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                    }
                }

                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun closeChatRoom(id: Long) {
        viewModelScope.launch {
            val result = chatUseCases.closeChatRoom(id)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun joinChatRoom(id: Long) {
        viewModelScope.launch {
            val result = chatUseCases.joinChatRoom(id)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                    }
                }

                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun checkWhoIsIn(id: Long) {
        viewModelScope.launch {
            val result = chatUseCases.checkWhoIsIn(id)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                        member = it
                    }
                }

                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun enterChatRoom(id: Long) {
        viewModelScope.launch {
            val result = chatUseCases.enterChatRoom(id)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { ChatUiState.Success(null) }
                        chattingRoom.value = it
                    }
                }

                is Resource.Error -> {
                    _state.update { ChatUiState.Error(result.message) }
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    _state.update { ChatUiState.Loading }
                }
            }
        }
    }

    fun navigateUp(route: Screen) {
        viewModelScope.launch {
            _event.emit(UIEvent.Navigate(route))
        }
    }

    fun connectSocket(roomId: Long) {
        viewModelScope.launch {
            getAccessToken().collect{ token->
                if(!token.isNullOrEmpty()){ // null뿐만 아니라 빈 값도 확인
                    chatUseCases.connectChatSocket(token, roomId, onConnected = {
                        subscribe(roomId)
                    }, onError = {
                        Log.d("socket", "error")
                    })
                } else {
                    _state.update { ChatUiState.Error("토큰이 없음") }
                }
            }
        }
    }

    private fun subscribe(roomId: Long) {
        viewModelScope.launch {
            chatUseCases.subscribeRoom(
                roomId,
                onMessage = {
                    //getMessages(roomId)
                },
                onUnreadUpdate = { unread ->
                    updateUnreadCount(unread)
                }
            )
        }
    }

    private fun updateUnreadCount(unread: UnreadBroadcast) {
        _message.update { pagingData->
            pagingData.map {
                if(it.messageId == unread.messageId){
                    it.copy(
                        unreadCount = unread.unreadCount
                    )
                }else{
                    it
                }
            }
        }
    }

    fun sendMessage(roomId: Long, content: String) {
        chatUseCases.sendMessage(roomId, content)
    }

    fun sendRead(roomId: Long) {
        chatUseCases.sendReadEvent(roomId)
    }

    fun disconnect() {
        chatUseCases.disconnect()
    }

    fun getMyName(){
        viewModelScope.launch {
            val result = getNickname()
            name = result!!
        }
    }
    fun getMessages(roomId: Long) {
        viewModelScope.launch {
            chatUseCases.getChatMessages(roomId)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _message.value = pagingData
                }
        }
    }
}