package com.stone.fridge.presentation.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.navigation.Screen
import com.stone.fridge.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    index: Int,
    navController: NavHostController,
    isUnread: Boolean,
    updateUnreadNotification: () -> Unit
){
    TopAppBar(
        modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
        title = {
            when(index){
                1 -> {
                    Text(
                        text = "홈",
                        fontFamily = CustomTheme.typography.headline3.fontFamily,
                        fontWeight = CustomTheme.typography.headline3.fontWeight,
                        fontSize = CustomTheme.typography.headline3.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
                4 -> {
                    Text(
                        text = "채팅",
                        fontFamily = CustomTheme.typography.headline3.fontFamily,
                        fontWeight = CustomTheme.typography.headline3.fontWeight,
                        fontSize = CustomTheme.typography.headline3.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    updateUnreadNotification()
                    navController.navigate(Screen.NotificationNav)
                }
            ) {
                if(isUnread){
                    Icon(
                        tint = Color.Unspecified,
                        imageVector = ImageVector.vectorResource(R.drawable.bell_new),
                        contentDescription = "alarm"
                    )
                }else{
                    Icon(
                        tint = CustomTheme.colors.iconDefault,
                        imageVector = ImageVector.vectorResource(R.drawable.bell),
                        contentDescription = "alarm"
                    )
                }
            }
        },
        colors = TopAppBarColors(
            containerColor = CustomTheme.colors.surface,
            scrolledContainerColor = CustomTheme.colors.surface,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        )
    )
}