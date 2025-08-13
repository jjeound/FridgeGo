package com.stone.fridge.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.stone.fridge.core.designsystem.theme.GoTheme
import com.stone.fridge.core.navigation.GoComposeNavigator
import com.stone.fridge.core.navigation.LocalComposeNavigator

@Composable
fun GoPreviewTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalComposeNavigator provides GoComposeNavigator(),
    ) {
        GoTheme {
            content()
        }
    }
}