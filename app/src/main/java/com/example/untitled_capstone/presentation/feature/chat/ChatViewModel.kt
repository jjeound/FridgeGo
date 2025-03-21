package com.example.untitled_capstone.presentation.feature.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.R
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.model.User
import com.example.untitled_capstone.presentation.feature.chat.event.ChattingAction
import com.example.untitled_capstone.presentation.feature.chat.state.ChatState
import com.example.untitled_capstone.presentation.feature.chat.state.MessageState

class ChatViewModel: ViewModel(){
    var chatState by mutableStateOf(ChatState())
        private set

    var messageState by mutableStateOf(MessageState())
        private set

//    init {
//        chatState = chatState.copy(
//            chats = listOf(
//                ChattingRoom(
//                    message = "message1",
//                    lastSentMessageTime = "2021-12-31",
//                    user = User(
//                        id = 1,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                    title = "title1",
//                    numberOfPeople = 1,
//                    messagesNotReadYet = 1
//                ),
//                ChattingRoom(
//                    message = "message1",
//                    lastSentMessageTime = "2021-12-31",
//                    user = User(
//                        id = 2,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                    title = "title2",
//                    numberOfPeople = 2,
//                    messagesNotReadYet = 2
//                ),
//                ChattingRoom(
//                    message = "message3",
//                    lastSentMessageTime = "2021-12-31",
//                    user = User(
//                        id = 3,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                    title = "title3",
//                    numberOfPeople = 3
//                ),
//                ChattingRoom(
//                    message = "message1",
//                    lastSentMessageTime = "2021-12-31",
//                    user = User(
//                        id = 4,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                    title = "title1",
//                    numberOfPeople = 4
//                ),
//            ),
//            isLoading = false
//        )
//        messageState = messageState.copy(
//            messages = listOf(
//                Message(
//                    message = "message1",
//                    time = "2025-01-24",
//                    user = User(
//                        id = 4,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                ),
//                Message(
//                    message = "message2",
//                    time = "2025-01-24",
//                    user = User(
//                        id = 4,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                ),
//                Message(
//                    message = "message3",
//                    time = "2025-01-24",
//                    user = User(
//                        id = 1,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                ),
//                Message(
//                    message = "message4",
//                    time = "2025-01-24",
//                    user = User(
//                        id = 1,
//                        name = "닉네임",
//                        profile = R.drawable.ic_launcher_background
//                    ),
//                ),
//            )
//        )
//    }

    fun onAction(action: ChattingAction){
        when(action){
            ChattingAction.GoToChattingRoom -> getMessages()
        }
    }

    fun getMessages(){
        //get messages from server
    }
}