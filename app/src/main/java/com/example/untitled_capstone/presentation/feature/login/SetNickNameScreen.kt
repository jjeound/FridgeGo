package com.example.untitled_capstone.presentation.feature.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetNickNameScreen(navigateToLoc: () -> Unit, popBackStack: () -> Unit, onEvent: (LoginEvent) -> Unit, state: LoginState) {
    var nickname by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
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
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
            ){
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    value = nickname,
                    onValueChange = {nickname = it},
                    placeholder = {
                        Text(
                            text = "닉네임",
                            style = CustomTheme.typography.body3,
                            color = CustomTheme.colors.textSecondary
                        )
                    },
                    trailingIcon = {
                        if(nickname.isNotBlank()){
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
                    ),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )
                LaunchedEffect(state.validate) {
                    if (state.validate) {
                        navigateToLoc()
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth().height(44.dp),
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
                        navigateToLoc()
                        onEvent(LoginEvent.SetNickname(nickname))
                        if(state.error != null){
                            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text(
                        text = "시작하기",
                        style = CustomTheme.typography.button1,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun SetNickNameScreenPreview(){
    SetNickNameScreen({}, {}, {}, LoginState())
}