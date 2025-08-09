package com.stone.fridge.feature.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.model.AccountInfo
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.login.navigation.NicknameRoute

@Composable
fun KakaoLogin(
    uiState: LoginUiState,
    login: (String) -> Unit,
    accountInfo : AccountInfo? = null,
    onLogin: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    val context = LocalContext.current
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState == LoginUiState.Success){
            if (accountInfo?.nickname != null){
                onLogin()
            } else {
                composeNavigator.navigate(NicknameRoute)
            }
        } else if (uiState is LoginUiState.Error) {
            onShowSnackbar(uiState.message, null)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = "logo"
        )
        Spacer(
            modifier = Modifier.height(Dimens.hugePadding)
        )
        Image(
            modifier = Modifier.width(600.dp).height(90.dp).padding(
                Dimens.largePadding
            ).clickable {
                //login("")
                kakaoLogin(context){ code ->
                    login(code)
                }
            },
            painter = painterResource(id = R.drawable.kakao_login_large_wide),
            contentDescription = "kakao_login"
        )
    }
}

private fun kakaoLogin(context: Context, onResult: (String) -> Unit){
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("Login", "카카오계정으로 로그인 실패")
        } else if (token != null) {
            onResult(token.accessToken)
            Log.d("Login", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }
// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.d("Login", "카카오톡으로 로그인 실패")

                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }

                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                onResult(token.accessToken)
                Log.d("Login", "카카오톡으로 로그인 성공 ${token.accessToken}")
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}

