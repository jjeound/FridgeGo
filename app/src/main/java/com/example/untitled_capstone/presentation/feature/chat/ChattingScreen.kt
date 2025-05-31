package com.example.untitled_capstone.presentation.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.ui.theme.CustomTheme


@Composable
fun ChattingScreen(
    uiState: ChatUiState,
    chattingRoomList: List<ChattingRoomRaw>,
    navigate: (Screen) -> Unit,
) {
    if(uiState == ChatUiState.Loading){
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
                    navigate(Screen.ChattingRoomNav(room.roomId, room.active))
                }
            ){
                ChatItem(chattingRoomRaw = room)
            }
        }
    }
}