package com.stone.fridge.feature.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.feature.post.navigation.PostSearchRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTopBar(
    location: String?,
    isUnread: Boolean,
    navigateToNotification:() -> Unit,
){
    val composeNavigator = currentComposeNavigator
    TopAppBar(
        modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
        title = {
            Row (
                modifier = Modifier.clickable {},
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = location ?: "",
                    fontFamily = CustomTheme.typography.headline3.fontFamily,
                    fontWeight = CustomTheme.typography.headline3.fontWeight,
                    fontSize = CustomTheme.typography.headline3.fontSize,
                    color = CustomTheme.colors.textPrimary,
                )
//                Spacer(modifier = Modifier.width(6.dp))
//                Icon(
//                    imageVector = ImageVector.vectorResource(R.drawable.chevron_down),
//                    contentDescription = "dropdown",
//                    tint = CustomTheme.colors.iconSelected
//                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    composeNavigator.navigate(PostSearchRoute)
                },
                enabled = location != null,
                colors = IconButtonDefaults.iconButtonColors(
                    disabledContentColor = CustomTheme.colors.buttonBorderUnfocused
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.search),
                    tint = CustomTheme.colors.iconDefault,
                    contentDescription = "search"
                )
            }
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
                }else {
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

@Preview
@Composable
fun PostTopBarPreview() {
    GoPreviewTheme {
        PostTopBar(
            location = "무거동",
            isUnread = true,
            navigateToNotification = {}
        )
    }
}