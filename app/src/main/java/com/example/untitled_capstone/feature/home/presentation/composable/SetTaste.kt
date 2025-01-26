package com.example.untitled_capstone.feature.home.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun SetTaste() {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.wrapContentHeight().fillMaxWidth()
    ){
        Column (
            modifier = Modifier.padding(Dimens.surfacePadding)
        ){
            Text(
                text = "내 취향 설정하기",
                fontFamily = CustomTheme.typography.title1.fontFamily,
                fontWeight = CustomTheme.typography.title1.fontWeight,
                fontSize = CustomTheme.typography.title1.fontSize,
                color = CustomTheme.colors.textPrimary,
                modifier = Modifier.
                padding(bottom = 2.dp).
                align(Alignment.Start)
            )
            Text(
                text = "내 취향에 맞는 레시피를 추천받아보세요!",
                fontFamily = CustomTheme.typography.caption1.fontFamily,
                fontWeight = CustomTheme.typography.caption1.fontWeight,
                fontSize = CustomTheme.typography.caption1.fontSize,
                color = CustomTheme.colors.textSecondary,
                modifier = Modifier.padding(bottom = 2.dp).
                align(Alignment.Start)
            )
            TextField(
                value = text,
                onValueChange = {text = it},
                placeholder = {
                    Text(
                        text = "나의 취향 입력하기!",
                        fontFamily = CustomTheme.typography.button2.fontFamily,
                        fontWeight = CustomTheme.typography.button2.fontWeight,
                        fontSize = CustomTheme.typography.button2.fontSize,
                        color = CustomTheme.colors.textSecondary
                    )
                },
                trailingIcon = {
                    if(text.isNotBlank()){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete),
                            contentDescription = "delete",
                            tint = CustomTheme.colors.iconDefault,
                            modifier = Modifier.clickable(
                                onClick = { text = "" }
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
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
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
    }

}

