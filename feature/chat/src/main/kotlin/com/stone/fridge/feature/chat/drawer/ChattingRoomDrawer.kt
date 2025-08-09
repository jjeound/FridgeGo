package com.stone.fridge.feature.chat.drawer

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.ChatMember
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.chat.detail.ChatDetailUiState
import com.stone.fridge.feature.chat.detail.ChatDetailViewModel
import com.stone.fridge.feature.chat.navigation.ChatRoute

@Composable
fun ChattingRoomDrawer(
    viewModel: ChatDetailViewModel,
    onShowSnackbar : suspend (String, String?) -> Unit ,
    roomId: Long,
    title: String,
    isActive: Boolean,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val members by viewModel.member.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.checkWhoIsIn(roomId)
        viewModel.getMyName()
    }
    if(uiState == ChatDetailUiState.Idle && name != null) {
        ChattingRoomDrawerContent(
            uiState = uiState,
            members = members,
            roomId = roomId,
            name = name!!,
            title = title,
            isActive = isActive,
            closeChatRoom = viewModel::closeChatRoom,
            exitChatRoom = viewModel::exitChatRoom,
            onShowSnackbar = onShowSnackbar
        )
    }else {
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
private fun ChattingRoomDrawerContent(
    uiState: ChatDetailUiState,
    members: List<ChatMember>,
    roomId: Long,
    name: String,
    title: String,
    isActive: Boolean,
    closeChatRoom: (Long) -> Unit,
    exitChatRoom: (Long) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    val scrollState = rememberScrollState()
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState == ChatDetailUiState.Success){
            composeNavigator.navigateAndClearBackStack(ChatRoute)
        } else if(uiState is ChatDetailUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { composeNavigator.navigateUp() }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.surface
                )
            )
        },
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(
                    horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding
                )
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.thumbnail,
                modifier = Modifier.size(80.dp).clip(CircleShape).border(
                    width = 1.dp,
                    color = CustomTheme.colors.border,
                    shape = CircleShape
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "chatting room image",
            )
            Text(
                modifier = Modifier.padding(Dimens.mediumPadding),
                text = title,
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomTheme.colors.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.largePadding),
                        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                    ) {
                        Text(
                            text = "참여자 ${members.size}",
                            style = CustomTheme.typography.title2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        members.forEach {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = Dimens.smallPadding,
                                        ).clickable{
//                                            navigate(
//                                                Screen.PostProfileNav(it.nickname)
//                                            )
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
                                ) {
                                    if(it.imageUrl != null){
                                        AsyncImage(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape),
                                            model = it.imageUrl,
                                            contentScale = ContentScale.Crop,
                                            contentDescription = "profile image",
                                        )
                                    }else{
                                        Icon(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape),
                                            imageVector = ImageVector.vectorResource(R.drawable.profile),
                                            contentDescription = "profile image",
                                            tint = CustomTheme.colors.iconDefault
                                        )
                                    }
                                    Text(
                                        text = it.nickname,
                                        style = CustomTheme.typography.title1,
                                        color = CustomTheme.colors.textPrimary,
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = CustomTheme.colors.borderLight
                                )
                            }
                        }
                    }
                }
            }
            if(members.any{it.host && it.nickname == name}){
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(Dimens.mediumPadding),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.largePadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomTheme.colors.iconRed,
                            contentColor = CustomTheme.colors.onPrimary,
                        ),
                        onClick = {
                            closeChatRoom(roomId)
                        },
                        enabled = isActive
                    ) {
                        Text(
                            text = "채팅방 마감하기",
                            style = CustomTheme.typography.button1,
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomTheme.colors.primary,
                            contentColor = CustomTheme.colors.onPrimary,
                        ),
                        onClick = {
                            exitChatRoom(roomId)
                        }
                    ) {
                        Text(
                            text = "채팅방 나가기",
                            style = CustomTheme.typography.button1,
                        )
                    }
                }
            } else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.mediumPadding),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colors.primary,
                        contentColor = CustomTheme.colors.onPrimary,
                    ),
                    onClick = {
                        exitChatRoom(roomId)
                    }
                ) {
                    Text(
                        text = "채팅방 나가기",
                        style = CustomTheme.typography.button1,
                    )
                }
            }
        }
    }
}