package com.example.untitled_capstone.presentation.feature.login.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.login.LoginEvent
import com.example.untitled_capstone.presentation.feature.login.state.ValidateState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetNickNameScreen(navigateToLoc: () -> Unit, popBackStack: () -> Unit, onEvent: (LoginEvent) -> Unit, state: ValidateState) {
    var nickname by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                title = {
                    Text(
                        text = "로그인",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {popBackStack()}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        value = nickname,
                        onValueChange = { nickname = it },
                        placeholder = {
                            Text(
                                text = "닉네임",
                                style = CustomTheme.typography.caption2,
                                color = CustomTheme.colors.textSecondary
                            )
                        },
                        trailingIcon = {
                            if (nickname.isNotBlank()) {
                                IconButton(
                                    onClick = { nickname = "" }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.delete),
                                        contentDescription = "delete",
                                    )
                                }
                            }
                        },
                        textStyle = CustomTheme.typography.body3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CustomTheme.colors.textSecondary,
                            unfocusedBorderColor = CustomTheme.colors.textSecondary,
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.onSurface,
                            unfocusedContainerColor = CustomTheme.colors.onSurface,
                            cursorColor = CustomTheme.colors.textPrimary,
                            errorCursorColor = CustomTheme.colors.error,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                            errorBorderColor = CustomTheme.colors.error,
                        ),
                        isError = state.validate,
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        singleLine = true,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    )
                    if(state.error != null){
                        Text(
                            text = "중복입니다.",
                            style = CustomTheme.typography.caption2,
                            color = CustomTheme.colors.error,
                        )
                    }
                }
                LaunchedEffect(state.validate) {
                    if (state.validate) {
                        navigateToLoc()
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colors.primary,
                        disabledContainerColor = CustomTheme.colors.onSurface,
                        contentColor = CustomTheme.colors.onPrimary,
                        disabledContentColor = CustomTheme.colors.textTertiary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = CustomTheme.colors.border
                    ),
                    onClick = {
                        onEvent(LoginEvent.SetNickname(nickname))
                    }
                ) {
                    Text(
                        text = "설정하기",
                        style = CustomTheme.typography.button1,
                    )
                }
                LaunchedEffect(state.validate) {
                    if(state.validate == true){
                        navigateToLoc()
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SetNickNameScreenPreview(){
    SetNickNameScreen({}, {}, {}, ValidateState())
}