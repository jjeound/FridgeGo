package com.example.untitled_capstone.presentation.feature.my.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyContainer(content: String, icon: Int, navigateTo: () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            navigateTo()
        },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = "icon",
                tint = CustomTheme.colors.iconSelected
            )
            Spacer(
                modifier = Modifier.padding(Dimens.smallPadding)
            )
            Text(
                text = content,
                style = CustomTheme.typography.body1,
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