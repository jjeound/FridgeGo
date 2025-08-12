package com.stone.fridge.feature.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stone.fridge.core.common.formatLocaleDateTimeToKoreanDateTime
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Notification
import com.stone.fridge.core.ui.GoPreviewTheme

@Composable
fun NotificationCard(
    notification: Notification
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if(notification.read) CustomTheme.colors.surface else CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.padding(vertical = Dimens.mediumPadding),
            ) {
                Text(
                    text = "소비기한",
                    style = CustomTheme.typography.body2,
                    color = CustomTheme.colors.textSecondary,
                )
                Text(
                    text = "${notification.ingredientName} ${notification.content ?: ""}",
                    style = CustomTheme.typography.body2,
                    color = CustomTheme.colors.textPrimary,
                )
            }
            Text(
                text = notification.scheduledAt.formatLocaleDateTimeToKoreanDateTime(),
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
        }
    }
}

@Preview
@Composable
fun NotificationCardPreview() {
    GoPreviewTheme {
        NotificationCard(
            notification = Notification(
                id = 1L,
                ingredientName = "사과",
                content = "소비기한이 곧 만료됩니다.",
                scheduledAt = "2023-10-01T12:00:00",
                read = false,
                status = ""
            )
        )
    }
}