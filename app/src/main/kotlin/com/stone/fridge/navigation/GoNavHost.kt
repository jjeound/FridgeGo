package com.stone.fridge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.feature.chat.navigation.chatNavigation
import com.stone.fridge.feature.chat.navigation.chattingDrawerNavigation
import com.stone.fridge.feature.chat.navigation.chattingRoomNavigation
import com.stone.fridge.feature.chat.navigation.navigateToChattingRoom
import com.stone.fridge.feature.fridge.navigation.fridgeCRUDNavigation
import com.stone.fridge.feature.fridge.navigation.fridgeNavigation
import com.stone.fridge.feature.fridge.navigation.fridgeScanNavigation
import com.stone.fridge.feature.home.navigation.homeNavigation
import com.stone.fridge.feature.home.navigation.navigateToHome
import com.stone.fridge.feature.home.navigation.recipeModifyNavigation
import com.stone.fridge.feature.home.navigation.recipeNavigation
import com.stone.fridge.feature.login.navigation.locationNavigation
import com.stone.fridge.feature.login.navigation.loginNavigation
import com.stone.fridge.feature.login.navigation.navigateToLocation
import com.stone.fridge.feature.login.navigation.navigateToLogin
import com.stone.fridge.feature.login.navigation.nicknameNavigation
import com.stone.fridge.feature.my.navigation.appInfoNavigation
import com.stone.fridge.feature.my.navigation.myNavigation
import com.stone.fridge.feature.my.navigation.myPostNavigation
import com.stone.fridge.feature.my.navigation.profileModifyNavigation
import com.stone.fridge.feature.my.navigation.profileNavigation
import com.stone.fridge.feature.notification.navigation.navigateToNotification
import com.stone.fridge.feature.notification.navigation.notifNavigation
import com.stone.fridge.feature.post.navigation.navigateToPostDetail
import com.stone.fridge.feature.post.navigation.postCRUDNavigation
import com.stone.fridge.feature.post.navigation.postDetailNavigation
import com.stone.fridge.feature.post.navigation.postNavigation
import com.stone.fridge.feature.post.navigation.postProfileNavigation
import com.stone.fridge.feature.post.navigation.postSearchNavigation
import com.stone.fridge.feature.post.navigation.reportNavigation
import com.stone.fridge.ui.onBoardingNavigation


@Composable
fun GoNavHost(
    navController: NavHostController,
    shouldShowBottomSheet: Boolean,
    hideBottomSheet: () -> Unit,
    startDestination: GoBaseRoute,
    isUnread: Boolean,
    updateUnreadNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination){
        homeNavigation(
            shouldShowBottomSheet = shouldShowBottomSheet,
            hideBottomSheet = hideBottomSheet,
            isUnread = isUnread,
            navigateToNotification = navController::navigateToNotification,
            onShowSnackbar = onShowSnackbar,
        ){
            recipeNavigation(
                onShowSnackbar = onShowSnackbar,
            )
            recipeModifyNavigation(
                onShowSnackbar = onShowSnackbar,
            )
        }
        postNavigation(
            isUnread = isUnread,
            navigateToNotification = navController::navigateToNotification,
            onShowSnackbar = onShowSnackbar
        ){
            postDetailNavigation(
                onShowSnackbar = onShowSnackbar,
                navigateToChattingRoom = navController::navigateToChattingRoom
            )
            postCRUDNavigation(
                onShowSnackbar = onShowSnackbar
            )
            postSearchNavigation(
                onShowSnackbar = onShowSnackbar,
            )
            postProfileNavigation(
                onShowSnackbar = onShowSnackbar,
            )
            reportNavigation(
                onShowSnackbar = onShowSnackbar,
            )
        }
        fridgeNavigation(
            isUnread = isUnread,
            navigateToNotification = navController::navigateToNotification,
            onShowSnackbar = onShowSnackbar,
        ){
            fridgeCRUDNavigation(
                onShowSnackbar = onShowSnackbar,
            )
            fridgeScanNavigation()
        }
        chatNavigation(
            isUnread = isUnread,
            navigateToNotification = navController::navigateToNotification,
            onShowSnackbar = onShowSnackbar
        ){}
        chattingRoomNavigation(
            onShowSnackbar = onShowSnackbar,
        )
        chattingDrawerNavigation(
            onShowSnackbar = onShowSnackbar,
        )
        myNavigation(
            onShowSnackbar = onShowSnackbar,
            navigateToLocation = navController::navigateToLocation
        ){
            profileNavigation(
                onLogout = navController::navigateToLogin,
                onShowSnackbar = onShowSnackbar,
            )
            profileModifyNavigation(
                onShowSnackbar = onShowSnackbar,
            )
            appInfoNavigation()
            myPostNavigation(
                navigateToPostDetail = navController::navigateToPostDetail
            )
        }
        loginNavigation(
            onShowSnackbar = onShowSnackbar,
            onLogin = navController::navigateToHome
        ){
            nicknameNavigation(
                onShowSnackbar = onShowSnackbar,
            )
        }
        locationNavigation(
            onShowSnackbar = onShowSnackbar,
            onLocationSet = navController::navigateToHome
        )
        notifNavigation(
            updateUnreadNotification = updateUnreadNotification,
            onShowSnackbar = onShowSnackbar,
        )
        onBoardingNavigation()
    }
}