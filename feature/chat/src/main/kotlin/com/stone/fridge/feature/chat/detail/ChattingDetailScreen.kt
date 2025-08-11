package com.stone.fridge.feature.chat.detail

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.ChatRoom
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.chat.navigation.ChatRoute
import com.stone.fridge.feature.chat.navigation.ChattingDrawerNav
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.model.ChatMember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingDetailScreen(
    viewModel: ChatDetailViewModel = hiltViewModel(),
    onProfileClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val roomId by viewModel.roomId.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messages = viewModel.message.collectAsLazyPagingItems()
    val members by viewModel.member.collectAsStateWithLifecycle()
    val chattingRoom by viewModel.chattingRoom.collectAsStateWithLifecycle()
    val userId by viewModel.userId.collectAsStateWithLifecycle()
    if(uiState == ChatDetailUiState.Idle && roomId != null && chattingRoom != null && userId != null) {
        ChattingRoomContent(
            uiState = uiState,
            roomId = roomId!!,
            chattingRoom = chattingRoom!!,
            messages = messages,
            members = members,
            userId = userId!!,
            sendRead = viewModel::sendRead,
            leaveRoom = viewModel::leaveRoom,
            sendMessage = viewModel::sendMessage,
            onShowSnackbar = onShowSnackbar,
            onProfileClick = onProfileClick
        )
    }else if(uiState == ChatDetailUiState.Loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    }
}

@Composable
private fun ChattingRoomContent(
    uiState: ChatDetailUiState,
    roomId: Long,
    chattingRoom: ChatRoom,
    messages: LazyPagingItems<Message>,
    members: List<ChatMember>,
    userId: Long,
    sendRead: (Long) -> Unit,
    leaveRoom: (Long) -> Unit,
    sendMessage: (Long, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
    onProfileClick: (String) -> Unit,
){
    val composeNavigator = currentComposeNavigator
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
    LaunchedEffect(uiState) {
        if(uiState is ChatDetailUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Row{
                        Text(
                            text = chattingRoom.name,
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(3.dp)
                        )
                        Text(
                            text = chattingRoom.currentParticipants.toString(),
                            style = CustomTheme.typography.title2,
                            color = CustomTheme.colors.textSecondary,
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            leaveRoom(roomId)
                            composeNavigator.navigateAndClearBackStack(ChatRoute)
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
                            composeNavigator.navigate(
                                ChattingDrawerNav(
                                    id = roomId,
                                    title = chattingRoom.name,
                                    isActive = chattingRoom.active
                                )
                            )
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
        Box(
            modifier = Modifier.padding(innerPadding).imePadding()
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding)
        ){
            ChattingRoomBody(
                messages = messages,
                chattingRoom = chattingRoom,
                members = members,
                userId = userId,
                roomId = roomId,
                sendMessage = sendMessage,
                sendRead = sendRead,
                onProfileClick = onProfileClick
            )
        }
    }
}

@Composable
private fun ChattingRoomBody(
    messages: LazyPagingItems<Message>,
    chattingRoom: ChatRoom,
    members: List<ChatMember>,
    userId: Long,
    roomId: Long,
    sendMessage: (Long, String) -> Unit,
    sendRead: (Long) -> Unit,
    onProfileClick: (String) -> Unit
){
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = messages.loadState) {
        if(messages.loadState.refresh is LoadState.Error) {
            Log.d("error", (messages.loadState.refresh as LoadState.Error).error.message.toString())
        }
    }
    LaunchedEffect(messages.itemCount) {
        val lastIndex = messages.itemCount - 1
        val lastMessage = if (lastIndex >= 0) messages.peek(lastIndex) else null

        if (lastMessage != null && lastMessage.senderId != userId) {
            Log.d("sendRead", "navigation: ${lastMessage.senderId} != $userId")
            // 마지막 메시지가 내가 보낸 메시지가 아닐 때만 읽음 처리
            sendRead(roomId)
        }
    }
    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
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
                items(messages.itemCount){ index ->
                    val message = messages[index]
                    if (message != null) {
                        val isMe = message.senderId == userId
                        val userNickname = members.find{ it.userId == message.senderId }?.nickname
                        val profileImage = members.find { it.userId == message.senderId }?.imageUrl
                        if(isMe){
                            MyMessageCard(
                                message = message,
                                isActive = chattingRoom.active,
                            )
                        } else {
                            YourMessageCard(
                                message = message,
                                userNickname = userNickname,
                                profileImage = profileImage,
                                isActive = chattingRoom.active,
                                onProfileClick = onProfileClick,
                            )
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(22.dp)
            )
            if(chattingRoom.active){
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