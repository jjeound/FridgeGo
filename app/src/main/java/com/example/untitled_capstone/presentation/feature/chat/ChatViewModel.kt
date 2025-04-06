package com.example.untitled_capstone.presentation.feature.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.use_case.chat.ChatUseCases
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.state.ChatApiState
import com.example.untitled_capstone.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
): ViewModel(){

    private val _state = mutableStateOf(ChatApiState())
    val state: State<ChatApiState> = _state

    private var _chattingRoomList = mutableStateOf<List<ChattingRoomRaw>>(emptyList())
    val chattingRoomList: State<List<ChattingRoomRaw>> = _chattingRoomList

    private var _chattingRoom = mutableStateOf<ChattingRoom?>(null)
    val chattingRoom: State<ChattingRoom?> = _chattingRoom

    private var _message = mutableStateOf<List<Message>>(emptyList())
    val message: State<List<Message>> = _message

    private var _member = mutableStateOf<List<ChatMember>>(emptyList())
    val member: State<List<ChatMember>> = _member

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun createChatRoom(name: String, maxParticipants: Int){
        viewModelScope.launch {
            val result = chatUseCases.createRoom(name, maxParticipants)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun readChats(id: Long){
        viewModelScope.launch {
            val result = chatUseCases.readChats(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun getMyRooms(){
        viewModelScope.launch {
            val result = chatUseCases.getMyRooms()
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _chattingRoomList.value = it
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _chattingRoomList.value = emptyList()
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _chattingRoomList.value = emptyList()
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun exitChatRoom(id: Long){
        viewModelScope.launch {
            val result = chatUseCases.exitChatRoom(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun closeChatRoom(id: Long){
        viewModelScope.launch {
            val result = chatUseCases.closeChatRoom(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun getMessages(id: Long, lastMessageId: Long? = null){
        viewModelScope.launch {
            val result = chatUseCases.getMessages(id, lastMessageId)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _message.value = it
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _message.value = emptyList()
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _message.value = emptyList()
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun joinChatRoom(id: Long){
        viewModelScope.launch {
            val result = chatUseCases.joinChatRoom(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun checkWhoIsIn(id: Long){
        viewModelScope.launch {
            val result = chatUseCases.checkWhoIsIn(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _member.value = it
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _member.value = emptyList()
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _member.value = emptyList()
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun enterChatRoom(id: Long){
        viewModelScope.launch {
            val result = chatUseCases.enterChatRoom(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.value.apply {
                            isSuccess = true
                            isLoading = false
                            error = null
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = false
                        error = result.message
                    }
                    _event.emit(UiEvent.ShowSnackbar(
                        result.message ?: "Unknown error"
                    ))
                }
                is Resource.Loading -> {
                    _state.value.apply {
                        isSuccess = false
                        isLoading = true
                        error = null
                    }
                }
            }
        }
    }

    fun navigateUp(route: Screen){
        viewModelScope.launch {
            _event.emit(UiEvent.Navigate(route))
        }
    }
}