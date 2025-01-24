package com.example.untitled_capstone.feature.chatting.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.feature.chatting.domain.model.ChattingRoom
import com.example.untitled_capstone.feature.chatting.domain.model.Message
import com.example.untitled_capstone.feature.chatting.domain.model.User
import com.example.untitled_capstone.feature.chatting.presentation.event.ChattingAction
import com.example.untitled_capstone.feature.chatting.presentation.state.ChatState
import com.example.untitled_capstone.feature.chatting.presentation.state.MessageState

class ChatViewModel: ViewModel(){
    var chatState by mutableStateOf(ChatState())
        private set

    var messageState by mutableStateOf(MessageState())
        private set

    init {
        chatState = chatState.copy(
            chats = listOf(
                ChattingRoom(
                    message = "message1",
                    lastSentMessageTime = "2021-12-31",
                    user = User(
                        id = 1,
                        profile = 1
                    ),
                    title = "title1",
                    numberOfPeople = 1,
                    messagesNotReadYet = 1
                ),
                ChattingRoom(
                    message = "message1",
                    lastSentMessageTime = "2021-12-31",
                    user = User(
                        id = 2,
                        profile = 1
                    ),
                    title = "title2",
                    numberOfPeople = 2,
                    messagesNotReadYet = 2
                ),
                ChattingRoom(
                    message = "message3",
                    lastSentMessageTime = "2021-12-31",
                    user = User(
                        id = 3,
                        profile = 1
                    ),
                    title = "title3",
                    numberOfPeople = 3
                ),
                ChattingRoom(
                    message = "message1",
                    lastSentMessageTime = "2021-12-31",
                    user = User(
                        id = 4,
                        profile = 1
                    ),
                    title = "title1",
                    numberOfPeople = 4
                ),
            ),
            isLoading = false
        )
        messageState = messageState.copy(
            messages = listOf(
                Message(
                    message = "message",
                    time = "2025-01-24",
                    user = User(
                        id = 4,
                        profile = 1
                    ),
                ),
                Message(
                    message = "message",
                    time = "2025-01-24",
                    user = User(
                        id = 4,
                        profile = 1
                    ),
                ),
                Message(
                    message = "message",
                    time = "2025-01-24",
                    user = User(
                        id = 1,
                        profile = 1
                    ),
                ),
                Message(
                    message = "message",
                    time = "2025-01-24",
                    user = User(
                        id = 1,
                        profile = 1
                    ),
                ),
            )
        )
    }

    fun onAction(action: ChattingAction){
        when(action){
            ChattingAction.GoToChattingRoom -> getMessages()
        }
    }

    fun getMessages(){
        //get messages from server
    }
}