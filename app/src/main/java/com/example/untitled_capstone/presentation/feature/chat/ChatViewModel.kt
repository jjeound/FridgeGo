package com.example.untitled_capstone.presentation.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.use_case.chat.ChatUseCases
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.state.ChatApiState
import com.example.untitled_capstone.presentation.feature.chat.state.ChattingRoomRawState
import com.example.untitled_capstone.presentation.feature.chat.state.ChattingRoomState
import com.example.untitled_capstone.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
): ViewModel() {

    val state = ChatApiState()

    private val _chattingRoomList = MutableStateFlow(ChattingRoomRawState())
    val chattingRoomList = _chattingRoomList.asStateFlow()

    private val _chattingRoom = MutableStateFlow(ChattingRoomState())
    val chattingRoom = _chattingRoom.asStateFlow()

    private val _message = MutableStateFlow<List<Message>>(emptyList())
    val message = _message.asStateFlow()

    private val _member = MutableStateFlow<List<ChatMember>>(emptyList())
    val member = _member.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun createChatRoom(name: String, maxParticipants: Int) {
        viewModelScope.launch {
            state.isLoading = true
            val result = chatUseCases.createRoom(name, maxParticipants)
            when (result) {
                is Resource.Success -> {
                    state.isSuccess = true
                    state.isLoading = false
                    state.error = null
                }

                is Resource.Error -> {
                    state.isSuccess = false
                    state.isLoading = false
                    state.error = result.message
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    state.isLoading = true
                }
            }
        }
    }

    fun readChats(id: Long) {
        viewModelScope.launch {
            state.isLoading = true
            val result = chatUseCases.readChats(id)
            when (result) {
                is Resource.Success -> {
                    state.isSuccess = true
                    state.isLoading = false
                    state.error = null
                }

                is Resource.Error -> {
                    state.isSuccess = false
                    state.isLoading = false
                    state.error = result.message
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    state.isSuccess = false
                    state.isLoading = true
                    state.error = null
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
                        _chattingRoomList.value.apply {
                            response = it
                            isLoading = false
                            error = null
                        }
                    }
                }

                is Resource.Error -> {
                    _chattingRoomList.value.apply {
                        response = emptyList()
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(
                        UiEvent.ShowSnackbar(
                            result.message ?: "Unknown error"
                        )
                    )
                }

                is Resource.Loading -> {
                    _chattingRoomList.value.apply {
                        response = emptyList()
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }
//
//    fun exitChatRoom(id: Long) {
//        viewModelScope.launch {
//            val result = chatUseCases.exitChatRoom(id)
//            when (result) {
//                is Resource.Success -> {
//                    result.data?.let {
//                        _state.value.apply {
//                            isSuccess = true
//                            isLoading = false
//                            error = null
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = false
//                        error = result.message
//                    }
//                    _event.emit(
//                        UiEvent.ShowSnackbar(
//                            result.message ?: "Unknown error"
//                        )
//                    )
//                }
//
//                is Resource.Loading -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = true
//                        error = null
//                    }
//                }
//            }
//        }
//    }
//
//    fun closeChatRoom(id: Long) {
//        viewModelScope.launch {
//            val result = chatUseCases.closeChatRoom(id)
//            when (result) {
//                is Resource.Success -> {
//                    result.data?.let {
//                        _state.value.apply {
//                            isSuccess = true
//                            isLoading = false
//                            error = null
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = false
//                        error = result.message
//                    }
//                    _event.emit(
//                        UiEvent.ShowSnackbar(
//                            result.message ?: "Unknown error"
//                        )
//                    )
//                }
//
//                is Resource.Loading -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = true
//                        error = null
//                    }
//                }
//            }
//        }
//    }
//
//    fun getMessages(id: Long, lastMessageId: Long? = null) {
//        viewModelScope.launch {
//            val result = chatUseCases.getMessages(id, lastMessageId)
//            when (result) {
//                is Resource.Success -> {
//                    result.data?.let {
//                        _message.value = it
//                        _state.value.apply {
//                            isSuccess = true
//                            isLoading = false
//                            error = null
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    _message.value = emptyList()
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = false
//                        error = result.message
//                    }
//                    _event.emit(
//                        UiEvent.ShowSnackbar(
//                            result.message ?: "Unknown error"
//                        )
//                    )
//                }
//
//                is Resource.Loading -> {
//                    _message.value = emptyList()
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = true
//                        error = null
//                    }
//                }
//            }
//        }
//    }
//
//    fun joinChatRoom(id: Long) {
//        viewModelScope.launch {
//            val result = chatUseCases.joinChatRoom(id)
//            when (result) {
//                is Resource.Success -> {
//                    result.data?.let {
//                        _state.value.apply {
//                            isSuccess = true
//                            isLoading = false
//                            error = null
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = false
//                        error = result.message
//                    }
//                    _event.emit(
//                        UiEvent.ShowSnackbar(
//                            result.message ?: "Unknown error"
//                        )
//                    )
//                }
//
//                is Resource.Loading -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = true
//                        error = null
//                    }
//                }
//            }
//        }
//    }
//
//    fun checkWhoIsIn(id: Long) {
//        viewModelScope.launch {
//            val result = chatUseCases.checkWhoIsIn(id)
//            when (result) {
//                is Resource.Success -> {
//                    result.data?.let {
//                        _member.value = it
//                        _state.value.apply {
//                            isSuccess = true
//                            isLoading = false
//                            error = null
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    _member.value = emptyList()
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = false
//                        error = result.message
//                    }
//                    _event.emit(
//                        UiEvent.ShowSnackbar(
//                            result.message ?: "Unknown error"
//                        )
//                    )
//                }
//
//                is Resource.Loading -> {
//                    _member.value = emptyList()
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = true
//                        error = null
//                    }
//                }
//            }
//        }
//    }
//
//    fun enterChatRoom(id: Long) {
//        viewModelScope.launch {
//            val result = chatUseCases.enterChatRoom(id)
//            when (result) {
//                is Resource.Success -> {
//                    result.data?.let {
//                        _state.value.apply {
//                            isSuccess = true
//                            isLoading = false
//                            error = null
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = false
//                        error = result.message
//                    }
//                    _event.emit(
//                        UiEvent.ShowSnackbar(
//                            result.message ?: "Unknown error"
//                        )
//                    )
//                }
//
//                is Resource.Loading -> {
//                    _state.value.apply {
//                        isSuccess = false
//                        isLoading = true
//                        error = null
//                    }
//                }
//            }
//        }
//    }

    fun navigateUp(route: Screen) {
        viewModelScope.launch {
            _event.emit(UiEvent.Navigate(route))
        }
    }

    fun connectSocket(token: String, roomId: Long) {
        chatUseCases.connectChatSocket(token, roomId, onConnected = {
            // 연결 성공 시 처리
        }, onError = {
            // 에러 처리
        })
    }

    fun subscribe(roomId: Long) {
        chatUseCases.subscribeRoom(
            roomId,
            onMessage = { message ->
                _message.value = _message.value + message
            },
            onUnreadUpdate = { unread ->
                print(unread.toString())
            }//푸시 알람
        )
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
}