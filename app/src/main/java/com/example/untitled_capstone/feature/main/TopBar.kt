package com.example.untitled_capstone.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(index: Int){
    TopAppBar(
        modifier = Modifier.padding(20.dp),
        title = {
            when(index){
                1 -> {
                    Text(
                        text = "",
                    )
                }
                4 -> {
                    Text(
                        text = "채팅",
                        fontFamily = CustomTheme.typography.headline3.fontFamily,
                        fontWeight = CustomTheme.typography.headline3.fontWeight,
                        fontSize = CustomTheme.typography.headline3.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
                5 -> {
                    Text(
                        text = "설정",
                        fontFamily = CustomTheme.typography.headline3.fontFamily,
                        fontWeight = CustomTheme.typography.headline3.fontWeight,
                        fontSize = CustomTheme.typography.headline3.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
            }
        },
        actions = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.search),
                tint = CustomTheme.colors.iconDefault,
                contentDescription = "search"
            )
            Icon(
                modifier = Modifier.padding(start = 24.dp),
                tint = CustomTheme.colors.iconDefault,
                imageVector = ImageVector.vectorResource(R.drawable.bell),
                contentDescription = "alarm"
            )
        },
        colors = TopAppBarColors(
            containerColor = CustomTheme.colors.surface,
            scrolledContainerColor = CustomTheme.colors.surface,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        )
    )
}