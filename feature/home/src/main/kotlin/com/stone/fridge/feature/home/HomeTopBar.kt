package com.stone.fridge.feature.home

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
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isUnread: Boolean,
    navigateToNotification: () -> Unit
){
    TopAppBar(
        modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
        title = {
            Text(
                text = "홈",
                fontFamily = CustomTheme.typography.headline3.fontFamily,
                fontWeight = CustomTheme.typography.headline3.fontWeight,
                fontSize = CustomTheme.typography.headline3.fontSize,
                color = CustomTheme.colors.textPrimary,
            )
        },
        actions = {
            IconButton(
                onClick = {
                    navigateToNotification()
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