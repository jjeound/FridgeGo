package com.example.untitled_capstone.presentation.feature.chat.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.composable.ChatItem
import com.example.untitled_capstone.presentation.feature.chat.state.ChatApiState
import com.example.untitled_capstone.presentation.util.UiEvent
import com.example.untitled_capstone.ui.theme.CustomTheme


@Composable
fun ChattingScreen(snackbarHostState: SnackbarHostState,viewModel: ChatViewModel, state: ChatApiState, navController: NavHostController) {
    val chattingRoomList by remember {viewModel.chattingRoomList}
    LaunchedEffect(Unit) {
        viewModel.getMyRooms()
    }
    LaunchedEffect(true) {
        viewModel.event.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
            }
        }
    }
    if(state.isLoading){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = CustomTheme.colors.primary
        )
    }
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding
        , vertical = Dimens.surfaceVerticalPadding)
    ) {
        items(chattingRoomList){ room ->
           Box(
               modifier = Modifier.clickable {
                   viewModel.navigateUp(Screen.ChattingRoomNav(room.roomId))
               }
           ){
               ChatItem(chattingRoomRaw = room)
           }
        }
    }
}