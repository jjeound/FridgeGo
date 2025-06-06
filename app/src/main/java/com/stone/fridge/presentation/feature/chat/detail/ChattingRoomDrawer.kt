package com.stone.fridge.presentation.feature.chat.detail

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.ChatMember
import com.stone.fridge.navigation.Screen
import com.stone.fridge.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingRoomDrawer(
    roomId: Long,
    title: String,
    isActive: Boolean,
    uiState: ChatDetailUiState,
    popBackStack: () -> Unit,
    members: List<ChatMember>,
    myName: String?,
    closeChatRoom: (Long) -> Unit,
    exitChatRoom: (Long) -> Unit,
    clearBackStack: () -> Unit,
    navigate: (Screen) -> Unit,
){
    val scrollState = rememberScrollState()
    if(uiState is ChatDetailUiState.Loading){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = CustomTheme.colors.primary
        )
    }

    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { popBackStack()}
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
                modifier = Modifier.size(80.dp),
                model = R.drawable.chattingroom_image,
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
                                            navigate(
                                                Screen.PostProfileNav(it.nickname)
                                            )
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
            if(members.any{it.host && it.nickname == myName}){
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
                            clearBackStack()
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
                            clearBackStack()
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
                        clearBackStack()
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