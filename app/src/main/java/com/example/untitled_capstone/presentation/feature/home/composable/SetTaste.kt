package com.example.untitled_capstone.presentation.feature.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.presentation.feature.home.state.TastePrefState
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun SetTaste(tastePrefState: TastePrefState, onEvent: (HomeEvent) -> Unit) {
    var text by remember { mutableStateOf(tastePrefState.tastePref ?: "") }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(tastePrefState.tastePref) {
        text = tastePrefState.tastePref ?: ""
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier.wrapContentHeight().fillMaxWidth()
    ){
        Column (
            modifier = Modifier.padding(Dimens.largePadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "내 취향 설정하기",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
            Text(
                text = "내 취향에 맞는 레시피를 추천받아보세요!",
                style = CustomTheme.typography.caption1,
                color = CustomTheme.colors.textSecondary,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = {text = it},
                placeholder = {
                    Text(
                        text = "나의 취향 입력하기!",
                        style = CustomTheme.typography.button2,
                        color = CustomTheme.colors.textSecondary
                    )
                },
                textStyle = CustomTheme.typography.button2,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = CustomTheme.colors.textPrimary,
                    unfocusedTextColor = CustomTheme.colors.textPrimary,
                    focusedContainerColor = CustomTheme.colors.surface,
                    unfocusedContainerColor = CustomTheme.colors.surface,
                    cursorColor = CustomTheme.colors.textPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                    unfocusedTrailingIconColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(Dimens.cornerRadius),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onEvent(HomeEvent.SetTastePreference(text))
                })
            )
        }
    }

}

