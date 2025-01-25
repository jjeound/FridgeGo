package com.example.untitled_capstone.feature.my.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyAccountContainer() {
    Card(
        shape = RoundedCornerShape(Dimens.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface
        )
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "profile",
                )
                //if user is logged in, show user name
                Text(
                    text = "로그인하기",
                    fontFamily = CustomTheme.typography.title1.fontFamily,
                    fontWeight = CustomTheme.typography.title1.fontWeight,
                    fontSize = CustomTheme.typography.title1.fontSize,
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