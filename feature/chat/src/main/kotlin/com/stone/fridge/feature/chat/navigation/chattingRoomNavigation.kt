package com.stone.fridge.feature.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.chat.detail.ChattingDetailScreen
import kotlinx.serialization.Serializable

@Serializable data class ChattingRoomNav(val id: Long, val isActive: Boolean): GoScreen

fun NavController.navigateToChattingRoom(id: Long, isActive: Boolean){
    navigate(route = ChattingRoomNav(id, isActive))
}

fun NavGraphBuilder.chattingRoomNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<ChattingRoomNav>{
        ChattingDetailScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}