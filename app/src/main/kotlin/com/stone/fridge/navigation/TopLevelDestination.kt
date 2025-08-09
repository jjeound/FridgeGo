package com.stone.fridge.navigation

import androidx.annotation.StringRes
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.chat.navigation.ChatRoute
import com.stone.fridge.feature.fridge.navigation.FridgeRoute
import com.stone.fridge.feature.home.navigation.HomeRoute
import com.stone.fridge.feature.my.navigation.MyRoute
import com.stone.fridge.feature.post.navigation.PostRoute
import com.stone.fridge.feature.home.R as HomeR
import com.stone.fridge.feature.chat.R as ChatR
import com.stone.fridge.feature.fridge.R as FridgeR
import com.stone.fridge.feature.my.R as MyR
import com.stone.fridge.feature.post.R as PostR

enum class TopLevelDestination(
    val icon: Int,
    @StringRes val label: Int,
    val route: GoScreen,
    @StringRes val contentDescription: Int,
) {
    HOME(
        icon = HomeR.drawable.home,
        label = HomeR.string.feature_home_title,
        route = HomeRoute,
        contentDescription = HomeR.string.feature_home_title,
    ),
    POST(
        icon = PostR.drawable.shopping,
        label = PostR.string.feature_post_title,
        route = PostRoute,
        contentDescription = PostR.string.feature_post_title,
    ),
    FRIDGE(
        icon = FridgeR.drawable.refrigerator,
        label = FridgeR.string.feature_fridge_title,
        route = FridgeRoute,
        contentDescription = FridgeR.string.feature_fridge_title,
    ),
    CHAT(
        icon = ChatR.drawable.chat,
        label = ChatR.string.feature_chat_title,
        route = ChatRoute,
        contentDescription = ChatR.string.feature_chat_title,
    ),
    MY(
        icon = MyR.drawable.my,
        label = MyR.string.feature_my_title,
        route = MyRoute,
        contentDescription = MyR.string.feature_my_title,
    ),
}