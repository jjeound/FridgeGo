package com.stone.fridge.feature.chat.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.chat.detail.ChatDetailViewModel
import com.stone.fridge.feature.chat.drawer.ChattingRoomDrawer
import kotlinx.serialization.Serializable

@Serializable data class ChattingDrawerNav(val id: Long, val title: String, val isActive: Boolean): GoScreen
fun NavGraphBuilder.chattingDrawerNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<ChattingDrawerNav>{
        val composeNavigator = currentComposeNavigator
        val parentEntry = composeNavigator.navControllerFlow.collectAsStateWithLifecycle().value?.getBackStackEntry(
            ChattingRoomNav)
        val viewModel: ChatDetailViewModel = hiltViewModel(parentEntry!!)
        val args = it.toRoute<ChattingDrawerNav>()
        ChattingRoomDrawer(
            viewModel = viewModel,
            onShowSnackbar = onShowSnackbar,
            roomId = args.id,
            title = args.title,
            isActive = args.isActive,
        )
    }
}