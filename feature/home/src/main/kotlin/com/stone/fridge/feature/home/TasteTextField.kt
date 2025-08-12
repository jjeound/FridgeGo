package com.stone.fridge.feature.home

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme

@Composable
fun TasteTextField(
    text: String,
    onValueChange: (String) -> Unit,
    setTastePreference: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // 금칙어 목록
    val bannedWords = listOf("지켜", "명령", "시스템", "프롬프트", "무시", "규칙 변경", "모든 규칙", "지침", "따르지 마", "override")

    // 에러 상태 계산
    val errorMessage = when {
        text.length > 50 -> "50자 이내로 입력해주세요."
        bannedWords.any { text.contains(it, ignoreCase = true) } -> "금칙어가 포함되어 있습니다."
        else -> null
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
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
                onValueChange = onValueChange,
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
                    errorContainerColor = CustomTheme.colors.surface,
                    errorIndicatorColor = Color.Transparent,
                    errorTextColor = CustomTheme.colors.error
                ),
                shape = RoundedCornerShape(Dimens.cornerRadius),
                isError = errorMessage != null,
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    setTastePreference(text)
                }),
            )

        }
    }

}

