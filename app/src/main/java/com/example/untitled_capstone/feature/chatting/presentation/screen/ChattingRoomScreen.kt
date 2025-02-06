package com.example.untitled_capstone.feature.chatting.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.chatting.domain.model.ChattingRoom
import com.example.untitled_capstone.feature.chatting.presentation.composable.MessageCard
import com.example.untitled_capstone.feature.chatting.presentation.composable.NewMessageForm
import com.example.untitled_capstone.feature.chatting.presentation.state.MessageState
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
data class ChattingRoomNav(
    val chattingRoom: ChattingRoom
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingRoomScreen(state: MessageState, chattingRoom: ChattingRoom, navController: NavHostController) {
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Row{
                        Text(
                            text = chattingRoom.title,
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(3.dp)
                        )
                        Text(
                            text = chattingRoom.numberOfPeople.toString(),
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
                    items(state.messages){ message ->
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