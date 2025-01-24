package com.example.untitled_capstone.feature.chatting.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.chatting.presentation.ChatViewModel
import com.example.untitled_capstone.feature.chatting.presentation.composable.ChatItem
import com.example.untitled_capstone.feature.chatting.presentation.state.ChatState


@Composable
fun ChattingScreen(state: ChatState, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.surfacePadding).padding(bottom = Dimens.onSurfacePadding)
    ) {
        items(state.chats){ chat ->
           Box(
               modifier = Modifier.clickable {
                   navController.navigate(ChattingRoomNav(
                       chattingRoom = chat
                   ))
               }
           ){
               ChatItem(chat = chat)
           }
        }
    }
}