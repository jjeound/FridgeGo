package com.example.untitled_capstone.feature.chatting.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import com.example.untitled_capstone.feature.chatting.domain.model.ChattingRoom
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun ChatItem(chat: ChattingRoom){
    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.onSurfacePadding),
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
                modifier = Modifier.padding(horizontal = Dimens.onSurfacePadding)
            )
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                Row{
                    Text(
                        text = chat.title,
                        fontFamily = CustomTheme.typography.title1.fontFamily,
                        fontWeight = CustomTheme.typography.title1.fontWeight,
                        fontSize = CustomTheme.typography.title1.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Spacer(
                        modifier = Modifier.padding(6.dp)
                    )
                    Text(
                        text = chat.numberOfPeople.toString(),
                        fontFamily = CustomTheme.typography.caption1.fontFamily,
                        fontWeight = CustomTheme.typography.caption1.fontWeight,
                        fontSize = CustomTheme.typography.caption1.fontSize,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
                Text(
                    text = chat.message,
                    fontFamily = CustomTheme.typography.body2.fontFamily,
                    fontWeight = CustomTheme.typography.body2.fontWeight,
                    fontSize = CustomTheme.typography.body2.fontSize,
                    color = CustomTheme.colors.textSecondary,
                )
            }
        }
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chat.lastSentMessageTime,
                fontFamily = CustomTheme.typography.caption1.fontFamily,
                fontWeight = CustomTheme.typography.caption1.fontWeight,
                fontSize = CustomTheme.typography.caption1.fontSize,
                color = CustomTheme.colors.textSecondary,
            )
            Spacer(
                modifier = Modifier.padding(6.dp)
            )
            if(chat.messagesNotReadYet > 0){
                Badge(
                    content = {
                        Text(
                            text = chat.messagesNotReadYet.toString(),
                            fontFamily = CustomTheme.typography.caption2.fontFamily,
                            fontWeight = CustomTheme.typography.caption2.fontWeight,
                            fontSize = CustomTheme.typography.caption2.fontSize,
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