package com.stone.fridge.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.stone.fridge.core.common.formatLocaleDateTimeToKoreanDateTime
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.ChatRoomRaw
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.chat.navigation.ChattingRoomNav


@Composable
fun ChattingScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    isUnread: Boolean,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val chattingRooms by viewModel.chattingRooms.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatTopbar(
            isUnread = isUnread,
            navigateToNotification = navigateToNotification
        )
        if(uiState == ChatUiState.Loading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = CustomTheme.colors.primary
                )
            }
        } else {
            ChattingContent(
                uiState = uiState,
                chattingRooms = chattingRooms,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@Composable
private fun ChattingContent(
    uiState: ChatUiState,
    chattingRooms: List<ChatRoomRaw>,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState is ChatUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    LazyColumn(
        modifier = Modifier.padding(
            horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ) {
        items(chattingRooms.size){ index ->
            val room = chattingRooms[index]
            ChatItem(
                room = room,
                onClick = {composeNavigator.navigate(ChattingRoomNav(room.roomId, room.active))}
            )
        }
    }
}

@Composable
private fun ChatItem(
    room: ChatRoomRaw,
    onClick: ()  -> Unit,
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if(room.active) CustomTheme.colors.onSurface else CustomTheme.colors.surface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier.fillMaxWidth().clickable{
            onClick()
        }
    ){
        Row (
            modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = R.drawable.thumbnail,
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(Dimens.cornerRadius)),
                    contentScale = ContentScale.Crop,
                    contentDescription = "thumbnail",
                )
                Spacer(
                    modifier = Modifier.width(Dimens.mediumPadding)
                )
                Column(
                    modifier = Modifier.padding(vertical = Dimens.smallPadding),
                ) {
                    Row{
                        Text(
                            text = room.name,
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false
                        )
                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )
                        Text(
                            text = room.currentParticipants.toString(),
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textSecondary,
                        )
                    }
                    Text(
                        text = room.lastMessage ?: "",
                        style = CustomTheme.typography.body2,
                        color = CustomTheme.colors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )
                }
            }
            Column(
                modifier = Modifier.padding(vertical = Dimens.smallPadding),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                if(room.lastMessageTime != null){
                    Text(
                        text = room.lastMessageTime!!.formatLocaleDateTimeToKoreanDateTime(),
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
                if(room.unreadCount > 0 && room.active){
                    val unreadCount = if(room.unreadCount > 100) "100+" else room.unreadCount.toString()
                    Badge(
                        content = {
                            Text(
                                text = unreadCount,
                                style = CustomTheme.typography.caption2,
                                color = Color.White
                            )
                        },
                        containerColor = CustomTheme.colors.iconRed,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }
    }
}