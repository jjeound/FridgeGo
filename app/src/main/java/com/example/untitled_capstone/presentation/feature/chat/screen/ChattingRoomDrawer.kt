package com.example.untitled_capstone.presentation.feature.chat.screen

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.state.ChatUiState
import com.example.untitled_capstone.presentation.util.UiEvent
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingRoomDrawer(
    roomId: Long,
    title: String,
    snackbarHostState: SnackbarHostState,
    viewModel: ChatViewModel,
    state: ChatUiState,
    navController: NavHostController
){
    val member =  viewModel.member
    val myName = viewModel.name
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.checkWhoIsIn(roomId)
    }
    LaunchedEffect(true) {
        viewModel.event.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.Navigate -> {
                    navController.navigate(event.route){
                        popUpTo(Screen.Chat){
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    if(state is ChatUiState.Loading){
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
                        onClick = { navController.popBackStack()}
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
                            text = "참여자 ${member.size}",
                            style = CustomTheme.typography.title2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        member.forEach {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = Dimens.smallPadding,
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape),
                                        model = it.imageUrl,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "profile image",
                                    )
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
            if(member.any{it.host && it.nickname == myName}){
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
                            viewModel.closeChatRoom(roomId)
                            viewModel.navigateUp(Screen.Chat)
                        }
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
                            viewModel.exitChatRoom(roomId)
                            viewModel.navigateUp(Screen.Chat)
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
                        viewModel.exitChatRoom(roomId)
                        viewModel.navigateUp(Screen.Chat)
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