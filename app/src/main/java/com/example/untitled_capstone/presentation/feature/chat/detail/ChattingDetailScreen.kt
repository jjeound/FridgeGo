package com.example.untitled_capstone.presentation.feature.chat.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingDetailScreen(
    roomId: Long,
    messages: LazyPagingItems<Message>,
    uiState: ChatDetailUiState,
    chattingRoom: ChattingRoom?,
    name: String?,
    clearBackStack: () -> Unit,
    disconnect: () -> Unit,
    navigate: (Screen) -> Unit,
    members: List<ChatMember>,
    sendMessage: (Long, String) -> Unit,
    leaveRoom: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                leaveRoom(roomId)
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                leaveRoom(roomId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if(uiState == ChatDetailUiState.Loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    }
    chattingRoom?.let { room ->
        Scaffold(
            containerColor = CustomTheme.colors.onSurface,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                    title = {
                        Row{
                            Text(
                                text = room.name,
                                style = CustomTheme.typography.title1,
                                color = CustomTheme.colors.textPrimary,
                            )
                            Spacer(
                                modifier = Modifier.width(3.dp)
                            )
                            Text(
                                text = room.currentParticipants.toString(),
                                style = CustomTheme.typography.title2,
                                color = CustomTheme.colors.textSecondary,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                disconnect()
                                clearBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "back",
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navigate(Screen.ChattingDrawerNav(roomId, room.name, room.active))
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.menu_hamburger),
                                contentDescription = "upload image",
                                tint = CustomTheme.colors.iconDefault
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CustomTheme.colors.onSurface
                    )
                )
            },
        ) { innerPadding ->
            LaunchedEffect(key1 = messages.loadState) {
                if(messages.loadState.refresh is LoadState.Error) {
                    Log.d("error", (messages.loadState.refresh as LoadState.Error).error.message.toString())
                }
            }
            Box(
                modifier = Modifier.padding(innerPadding).imePadding()
                    .padding(horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(messages.loadState.refresh is LoadState.Loading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(
                                color = CustomTheme.colors.primary
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            state = listState,
                            reverseLayout = true,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
//                            item {
//                                Box(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    contentAlignment = Alignment.Center
//                                ){
//                                    Text(
//                                        text = "hi님이 들어왔습니다.",
//                                        style = CustomTheme.typography.caption2,
//                                        color = CustomTheme.colors.textSecondary,
//                                    )
//                                }
//                            }
                            items(messages.itemCount){ index ->
                                val message = messages[index]
                                if (message != null) {
                                    val isMe = message.senderNickname == name
                                    val profileImage = members.find { it.nickname == message.senderNickname }?.imageUrl
                                    MessageCard(
                                        message = message,
                                        isMe = isMe,
                                        profileImage = profileImage,
                                        isActive = room.active,
                                        navigate = navigate
                                    )
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )
                        if(room.active){
                            NewMessageForm(
                                roomId = roomId,
                                sendMessage = sendMessage,
                            )
                        }else{
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "채팅방이 비활성화 되었습니다.",
                                    style = CustomTheme.typography.body2,
                                    color = CustomTheme.colors.textSecondary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}