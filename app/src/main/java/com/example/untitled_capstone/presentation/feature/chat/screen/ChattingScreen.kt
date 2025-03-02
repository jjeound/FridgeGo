package com.example.untitled_capstone.presentation.feature.chat.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.composable.ChatItem
import com.example.untitled_capstone.presentation.feature.chat.state.ChatState


@Composable
fun ChattingScreen(state: ChatState, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding
        , vertical = Dimens.surfaceVerticalPadding)
    ) {
        items(state.chats){ chat ->
           Box(
               modifier = Modifier.clickable {
                   navController.navigate(
                       Screen.ChattingRoomNav(
                           chattingRoom = chat
                       )
                   )
               }
           ){
               ChatItem(chat = chat)
           }
        }
    }
}