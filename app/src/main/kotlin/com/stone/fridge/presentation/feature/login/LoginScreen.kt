package com.stone.fridge.presentation.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.AccountInfo
import com.stone.fridge.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    login: (String) -> Unit,
    accountInfo: AccountInfo?,
    navigateToHome: () -> Unit,
    navigateToNext: () -> Unit,
){
    LaunchedEffect(uiState) {
        if(uiState == LoginUiState.Success){
            if (accountInfo?.nickname != null){
                navigateToHome()
            } else {
                navigateToNext()
            }
        }
    }
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
            )
        }
    }
}
