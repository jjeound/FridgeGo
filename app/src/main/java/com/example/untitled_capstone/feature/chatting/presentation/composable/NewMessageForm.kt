package com.example.untitled_capstone.feature.chatting.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun NewMessageForm(){
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.plus),
            contentDescription = "more",
            tint = CustomTheme.colors.iconDefault,
        )
        TextField(
            value = text,
            onValueChange = {text = it},
            placeholder = {
                Text(
                    text = "메시지 보내기",
                    style = CustomTheme.typography.button2,
                    color = CustomTheme.colors.textSecondary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.emoji),
                        contentDescription = "emoji",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
            },
            modifier = Modifier.weight(1f),
            textStyle = CustomTheme.typography.button2,
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.surface,
                unfocusedContainerColor = CustomTheme.colors.surface,
                cursorColor = CustomTheme.colors.textPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(Dimens.cornerRadius),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.send),
                contentDescription = "send message",
                tint = CustomTheme.colors.iconDefault
            )
        }
    }
}