package com.stone.fridge.presentation.feature.my

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar() {
    TopAppBar(
        modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
        title = {
            Text(
                text = "My",
                style = CustomTheme.typography.headline3,
                color = CustomTheme.colors.textPrimary,
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