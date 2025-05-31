package com.example.untitled_capstone.presentation.feature.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ChatItem(chattingRoomRaw: ChattingRoomRaw){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if(chattingRoomRaw.active) CustomTheme.colors.onSurface else CustomTheme.colors.surface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = R.drawable.thumbnail,
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(Dimens.cornerRadius)),
                    contentScale = ContentScale.Crop,
                    contentDescription = "thumbnail",
                )
                Spacer(
                    modifier = Modifier.width(Dimens.mediumPadding)
                )
                Column(
                    modifier = Modifier.padding(vertical = Dimens.smallPadding),
                ) {
                    Row{
                        Text(
                            text = chattingRoomRaw.name,
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )
                        Text(
                            text = chattingRoomRaw.currentParticipants.toString(),
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textSecondary,
                        )
                    }
                    Text(
                        text = chattingRoomRaw.lastMessage ?: "",
                        style = CustomTheme.typography.body2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(vertical = Dimens.smallPadding),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                if(chattingRoomRaw.lastMessageTime != null){
                    Text(
                        text = formatLocaleDateTimeToKoreanDateTime(chattingRoomRaw.lastMessageTime),
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
                if(chattingRoomRaw.unreadCount > 0 && chattingRoomRaw.active){
                    Badge(
                        content = {
                            Text(
                                text = chattingRoomRaw.unreadCount.toString(),
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
}

fun formatLocaleDateTimeToKoreanDateTime(localDateTime: LocalDateTime): String {
    val koreaTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))

    val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    val today = now.toLocalDate()
    val yesterday = today.minusDays(1)

    val date = koreaTime.toLocalDate()

    return when (date) {
        today -> {
            val timeFormatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN)
            koreaTime.format(timeFormatter)
        }
        yesterday -> "어제"
        else -> {
            val dateFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
            koreaTime.format(dateFormatter)
        }
    }
}