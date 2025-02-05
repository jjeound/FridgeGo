package com.example.untitled_capstone.feature.my.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "profile",
                )
                //if user is logged in, show user name
                Text(
                    text = "로그인하기",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                )
            }
            IconButton(
                modifier = Modifier.then(Modifier.size(24.dp)),
                onClick = {}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                    contentDescription = "navigate",
                    tint = CustomTheme.colors.iconDefault
                )
            }
        }
    }
}