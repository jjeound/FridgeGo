package com.example.untitled_capstone.presentation.feature.chat.screen

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.composable.ChatItem
import com.example.untitled_capstone.presentation.feature.chat.state.ChatUiState
import com.example.untitled_capstone.presentation.util.UiEvent
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.time.LocalDateTime


@Composable
fun ChattingScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: ChatViewModel,
    state: ChatUiState,
    navController: NavHostController
) {
    val chattingRoomList = viewModel.chattingRoomList
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
    if(state is ChatUiState.Loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    }
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding
            , vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ) {
        items(chattingRoomList.size){ index ->
            val room = chattingRoomList[index]
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