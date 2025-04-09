package com.example.untitled_capstone.presentation.feature.chat.composable

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MessageCard(message: Message, isMe: Boolean, profileImage: String?) {
    if(isMe){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = formatUtcToKoreanDateTime(message.sentAt),
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
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
                        text = message.content,
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
            }
        }
    }else{
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
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "get image",
                    tint = CustomTheme.colors.iconDefault,
                )
            }
            Spacer(
                modifier = Modifier.width(6.dp)
            )
            Column {
                Text(
                    text = message.senderNickname,
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
                            text = message.content,
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier.width(Dimens.smallPadding)
            )
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = formatUtcToKoreanDateTime(message.sentAt),
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
        }
    }
    Spacer(
        modifier = Modifier.height(Dimens.mediumPadding)
    )
}