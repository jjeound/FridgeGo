package com.example.untitled_capstone.presentation.feature.notification.composable

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
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Notification
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun NotificationCard(notification: Notification) {
    Card(
        onClick = {  },
        colors = CardDefaults.cardColors(
            containerColor = if(notification.isRead) CustomTheme.colors.surface else CustomTheme.colors.onSurface,
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
                    text = notification.title,
                    style = CustomTheme.typography.body2,
                    color = CustomTheme.colors.textSecondary,
                )
                Text(
                    text = notification.content,
                    style = CustomTheme.typography.body2,
                    color = CustomTheme.colors.textPrimary,
                )
            }
            Text(
                text = notification.time,
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
        }
    }
}

@Preview
@Composable
fun NotificationCardPreview() {
    NotificationCard(Notification("title", "content", "time", false, "id"))
}