package com.example.untitled_capstone.presentation.feature.chat.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun ChatItem(chat: ChattingRoom){
    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.mediumPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.profile),
                contentDescription = "profile",
                modifier = Modifier.size(48.dp).clip(
                    shape = RoundedCornerShape(24.dp)
                )
            )
            Spacer(
                modifier = Modifier.width(Dimens.mediumPadding)
            )
            Column(
                modifier = Modifier.padding(vertical = Dimens.smallPadding),
            ) {
                Row{
                    Text(
                        text = chat.title,
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )
                    Text(
                        text = chat.numberOfPeople.toString(),
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
                Text(
                    text = chat.message,
                    style = CustomTheme.typography.body2,
                    color = CustomTheme.colors.textSecondary,
                )
            }
        }
        Column(
            modifier = Modifier.padding(vertical = Dimens.smallPadding),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chat.lastSentMessageTime,
                style = CustomTheme.typography.caption1,
                color = CustomTheme.colors.textSecondary,
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )
            if(chat.messagesNotReadYet > 0){
                Badge(
                    content = {
                        Text(
                            text = chat.messagesNotReadYet.toString(),
                            style = CustomTheme.typography.caption2,
                            color = Color.White
                        )
                    },
                    containerColor = CustomTheme.colors.iconRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}