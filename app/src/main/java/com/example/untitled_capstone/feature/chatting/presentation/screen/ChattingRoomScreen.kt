package com.example.untitled_capstone.feature.chatting.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.chatting.domain.model.ChattingRoom
import com.example.untitled_capstone.feature.chatting.presentation.composable.MessageCard
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
                modifier = Modifier.padding(Dimens.surfacePadding),
                title = {
                    Text(
                        text = chattingRoom.title,
                        fontFamily = CustomTheme.typography.headline3.fontFamily,
                        fontWeight = CustomTheme.typography.headline3.fontWeight,
                        fontSize = CustomTheme.typography.headline3.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                        tint = CustomTheme.colors.iconDefault,
                        contentDescription = "back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                },
                actions = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.more),
                        contentDescription = "upload image",
                        tint = CustomTheme.colors.iconDefault
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            LazyColumn(
            ) {
                items(state.messages){ message ->
                    MessageCard(message = message)
                }
            }
        }
    }
}