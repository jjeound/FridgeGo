package com.example.untitled_capstone.presentation.feature.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyAccountContainer(
    navigateUp: () -> Unit,
    nickname: String?,
    image: String?
) {
    Card(
        modifier = Modifier.clickable { navigateUp()},
        shape = RoundedCornerShape(Dimens.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface
        )
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                image?.let {
                    AsyncImage(
                        modifier = Modifier.clip(
                            CircleShape
                        ).size(36.dp),
                        model = it,
                        contentDescription = "profile",
                    )
                } ?: Image(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "profile",
                )
                Text(
                    text = nickname ?: "User",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                )
            }
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                contentDescription = "navigate",
                tint = CustomTheme.colors.iconDefault
            )
        }
    }
}