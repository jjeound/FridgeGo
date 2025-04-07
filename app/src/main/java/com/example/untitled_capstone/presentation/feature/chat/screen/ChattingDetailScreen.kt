package com.example.untitled_capstone.presentation.feature.chat.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.composable.MessageCard
import com.example.untitled_capstone.presentation.feature.chat.composable.NewMessageForm
import com.example.untitled_capstone.presentation.feature.chat.state.ChatApiState
import com.example.untitled_capstone.presentation.util.UiEvent
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingDetailScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: ChatViewModel,
    state: ChatApiState,
    roomId: Long,
    navController: NavHostController
) {
    val messages by remember {viewModel.message}
    val chattingRoom by remember { viewModel.chattingRoom }
    LaunchedEffect(Unit) {
        viewModel.getMessages(roomId)
        viewModel.readChats(roomId)
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
    chattingRoom?.let {
        Scaffold(
            containerColor = CustomTheme.colors.onSurface,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                    title = {
                        Row{
                            Text(
                                text = chattingRoom!!.name,
                                style = CustomTheme.typography.title1,
                                color = CustomTheme.colors.textPrimary,
                            )
                            Spacer(
                                modifier = Modifier.width(3.dp)
                            )
                            Text(
                                text = chattingRoom!!.currentParticipants.toString(),
                                style = CustomTheme.typography.title2,
                                color = CustomTheme.colors.textSecondary,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                    },
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
                    actions = {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.more),
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
                modifier = Modifier.padding(innerPadding)
                    .padding(horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding)
            ){
                Column {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom,
                        reverseLayout = true
                    ) {
                        items(messages){ message ->
                            MessageCard(message = message)
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )
                    NewMessageForm()
                }
            }
        }
    }
}