package com.stone.fridge.presentation.feature.chat.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.ChatMember
import com.stone.fridge.domain.model.ChattingRoom
import com.stone.fridge.domain.model.ChattingRoomRaw
import com.stone.fridge.domain.model.Message
import com.stone.fridge.domain.model.UnreadBroadcast
import com.stone.fridge.domain.use_case.chat.CheckWhoIsInUseCase
import com.stone.fridge.domain.use_case.chat.CloseChatRoomUseCase
import com.stone.fridge.domain.use_case.chat.ConnectChatSocketUseCase
import com.stone.fridge.domain.use_case.chat.DisconnectUseCase
import com.stone.fridge.domain.use_case.chat.EnterChatRoomUseCase
import com.stone.fridge.domain.use_case.chat.ExitChatRoomUseCase
import com.stone.fridge.domain.use_case.chat.FCMEnterRoomUseCase
import com.stone.fridge.domain.use_case.chat.FCMLeaveRoomUseCase
import com.stone.fridge.domain.use_case.chat.GetMessagesUseCase
import com.stone.fridge.domain.use_case.chat.GetMyChatRoomsUseCase
import com.stone.fridge.domain.use_case.chat.GetPagedMessagesUseCase
import com.stone.fridge.domain.use_case.chat.GetUserIdUseCase
import com.stone.fridge.domain.use_case.chat.JoinChatRoomUseCase
import com.stone.fridge.domain.use_case.chat.ReadChatUseCase
import com.stone.fridge.domain.use_case.chat.SendMessageUseCase
import com.stone.fridge.domain.use_case.chat.SendReadEventUseCase
import com.stone.fridge.domain.use_case.chat.SubscribeRoomUseCase
import com.stone.fridge.domain.use_case.my.GetAccessTokenUseCase
import com.stone.fridge.domain.use_case.my.GetMyNicknameUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMyNicknameUseCase: GetMyNicknameUseCase,
    private val getPagedMessagesUseCase: GetPagedMessagesUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val enterChatRoomUseCase: EnterChatRoomUseCase,
    private val readChatUseCase: ReadChatUseCase,
    private val joinChatRoomUseCase: JoinChatRoomUseCase,
    private val exitChatRoomUseCase: ExitChatRoomUseCase,
    private val checkWhoIsInUseCase: CheckWhoIsInUseCase,
    private val closeChatRoomUseCase: CloseChatRoomUseCase,
    private val connectChatSocketUseCase: ConnectChatSocketUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val subscribeRoomUseCase: SubscribeRoomUseCase,
    private val disconnectUseCase: DisconnectUseCase,
    private val sendReadChatUseCase: ReadChatUseCase,
    private val sendReadEventUseCase: SendReadEventUseCase,
    private val getMyRoomsUseCase: GetMyChatRoomsUseCase,
    private val fcmEnterRoomUseCase: FCMEnterRoomUseCase,
    private val fcmLeaveRoomUseCase: FCMLeaveRoomUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
): ViewModel() {

    val uiState: MutableStateFlow<ChatDetailUiState> = MutableStateFlow<ChatDetailUiState>(ChatDetailUiState.Loading)

    private val _chattingRoom = MutableStateFlow<ChattingRoom?>(null)
    val chattingRoom = _chattingRoom.asStateFlow()

    private val _chattingRoomList = MutableStateFlow<List<ChattingRoomRaw>>(emptyList())
    val chattingRoomList = _chattingRoomList.asStateFlow()

    private val _message: MutableStateFlow<PagingData<Message>> = MutableStateFlow(PagingData.empty())
    val message = _message.asStateFlow()

    private val _member = MutableStateFlow<List<ChatMember>>(emptyList())
    val member = _member.asStateFlow()

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getMessages(roomId: Long) {
        viewModelScope.launch {
            getPagedMessagesUseCase(roomId)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _message.value = pagingData
                }
        }
        uiState.tryEmit(ChatDetailUiState.Idle)
    }

    fun getMyRooms() {
        viewModelScope.launch {
            getMyRoomsUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { rooms ->
                            _chattingRoomList.update { rooms }
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun enterChatRoom(id: Long) {
        viewModelScope.launch {
            enterChatRoomUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { room ->
                            _chattingRoom.value = room
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun readChats(id: Long) {
        viewModelScope.launch {
            readChatUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { room ->
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun joinChatRoom(id: Long) {
        viewModelScope.launch {
            joinChatRoomUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { room ->
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun getMyName(){
        viewModelScope.launch {
            _name.update{getMyNicknameUseCase()}
        }
    }

    fun getUserId() {
        viewModelScope.launch {
            _userId.update { getUserIdUseCase() }
        }
    }

    fun checkWhoIsIn(id: Long) {
        viewModelScope.launch {
            checkWhoIsInUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { members ->
                            _member.value = members
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun closeChatRoom(id: Long) {
        viewModelScope.launch {
            closeChatRoomUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun exitChatRoom(id: Long) {
        viewModelScope.launch {
            exitChatRoomUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let { message ->
                            uiState.tryEmit(ChatDetailUiState.Idle)
                        }
                    }

                    is Resource.Error -> {
                        uiState.tryEmit(ChatDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        uiState.tryEmit(ChatDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun connectSocket(roomId: Long) {
        viewModelScope.launch {
            val token = getAccessTokenUseCase()
            if(!token.isNullOrBlank()){ // null뿐만 아니라 빈 값도 확인
                connectChatSocketUseCase(token, roomId, onConnected = {
                    subscribe(roomId)
                    readChats(roomId)
                }, onError = {
                    Log.d("socket", "error")
                })
            } else {
                _event.emit(UiEvent.ShowSnackbar("Token is null or blank"))
            }
        }
    }

    private fun subscribe(roomId: Long) {
        viewModelScope.launch {
            subscribeRoomUseCase(
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
        viewModelScope.launch {
            val currentMessages = _message.value

            val updated = currentMessages.map { message ->
                if (message.messageId == unread.messageId) {
                    message.copy(unreadCount = unread.unreadCount)
                } else message
            }

            _message.update {updated}
        }
    }

    fun sendMessage(roomId: Long, content: String) {
        sendMessageUseCase(roomId, content)
    }

    fun sendRead(roomId: Long) {
        sendReadEventUseCase(roomId)
    }

    fun disconnect() {
        disconnectUseCase()
    }

    fun fcmEnterRoom(roomId: Long){
        fcmEnterRoomUseCase(roomId)
    }

    fun fcmLeaveRoom(roomId: Long){
        fcmLeaveRoomUseCase(roomId)
    }
}

interface ChatDetailUiState {
    data object Idle : ChatDetailUiState
    data object Loading : ChatDetailUiState
    data class Error(val message: String?) : ChatDetailUiState
}