package com.stone.fridge.feature.chat.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stone.fridge.core.common.formatLocaleDateTimeToKoreanDateTime
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.navigation.currentComposeNavigator

@Composable
fun MyMessageCard(
    message: Message,
    isActive: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        if(isActive && message.unreadCount > 0){
            Column(
                modifier = Modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = message.unreadCount.toString(),
                    style = CustomTheme.typography.caption2,
                    color = CustomTheme.colors.iconRed,
                )
                Text(
                    text = message.sentAt.formatLocaleDateTimeToKoreanDateTime(),
                    style = CustomTheme.typography.caption2,
                    color = CustomTheme.colors.textSecondary,
                )
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = message.sentAt.formatLocaleDateTimeToKoreanDateTime(),
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
        }
        Spacer(
            modifier = Modifier.width(Dimens.smallPadding)
        )
        Card(
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.surface
            )
        ) {
            Box(
                modifier = Modifier.padding(
                    horizontal = Dimens.mediumPadding,
                    vertical = Dimens.smallPadding
                )
            ) {
                Text(
                    modifier = Modifier.padding(Dimens.smallPadding).widthIn(max = 200.dp),
                    text = message.content,
                    style = CustomTheme.typography.caption1,
                    color = CustomTheme.colors.textPrimary,
                    softWrap = true
                )
            }
        }
    }
    Spacer(
        modifier = Modifier.height(Dimens.mediumPadding)
    )
}

@Composable
fun YourMessageCard(
    message: Message,
    userNickname: String?,
    profileImage: String?,
    isActive: Boolean,
){
    val composeNavigator = currentComposeNavigator
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        if(profileImage != null){
            AsyncImage(
                model = profileImage,
                contentDescription = "profile image",
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = RoundedCornerShape(36.dp))
                    .clickable {
//                        navigate(Screen.PostProfileNav(
//                            message.senderNickname
//                        ))
                        //composeNavigator.navigate()
                    }
            )
        } else {
            Icon(
                modifier = Modifier.clickable{
//                    navigate(Screen.PostProfileNav(
//                        message.senderNickname
//                    ))
                },
                imageVector = ImageVector.vectorResource(R.drawable.profile),
                contentDescription = "get image",
                tint = CustomTheme.colors.iconDefault,
            )
        }
        Spacer(
            modifier = Modifier.width(6.dp)
        )
        Column{
            Text(
                text = userNickname ?: message.senderNickname,
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textPrimary,
            )
            Card(
                shape = RoundedCornerShape(Dimens.cornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = CustomTheme.colors.surface
                )
            ) {
                Box(
                    modifier = Modifier.padding(
                        horizontal = Dimens.mediumPadding,
                        vertical = Dimens.smallPadding
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(Dimens.smallPadding).widthIn(max = 200.dp),
                        text = message.content,
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textPrimary,
                        softWrap = true
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.width(Dimens.smallPadding)
        )
        if(isActive && message.unreadCount > 0){
            Column(
                modifier = Modifier.height(36.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = message.unreadCount.toString(),
                    style = CustomTheme.typography.caption2,
                    color = CustomTheme.colors.iconRed,
                )
                Text(
                    text = message.sentAt.formatLocaleDateTimeToKoreanDateTime(),
                    style = CustomTheme.typography.caption2,
                    color = CustomTheme.colors.textSecondary,
                )
            }
        } else {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom),
                text = message.sentAt.formatLocaleDateTimeToKoreanDateTime(),
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
        }

    }
    Spacer(
        modifier = Modifier.height(Dimens.mediumPadding)
    )
}