package com.stone.fridge.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.AccountInfo
import com.stone.fridge.core.ui.GoPreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
    onLogin: () -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val accountInfo by viewModel.accountInfo.collectAsStateWithLifecycle()
    LoginScreenContent(
        uiState = uiState,
        accountInfo = accountInfo,
        login = viewModel::login,
        onLogin = onLogin,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
internal fun LoginScreenContent(
    uiState: LoginUiState,
    accountInfo: AccountInfo?,
    login: (String) -> Unit,
    onLogin: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            KakaoLogin(
                uiState = uiState,
                login = login,
                accountInfo = accountInfo,
                onLogin = onLogin,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenContentPreview() {
    GoPreviewTheme {
        LoginScreenContent(
            uiState = LoginUiState.Idle,
            accountInfo = null,
            login = {},
            onLogin = {},
            onShowSnackbar = { _, _ -> }
        )
    }
}