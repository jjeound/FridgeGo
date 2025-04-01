package com.example.untitled_capstone.presentation.feature.login.composable

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.navigation.Graph
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.login.LoginEvent
import com.example.untitled_capstone.presentation.feature.login.state.LoginState
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

@Composable
fun KakaoLogin(state: LoginState, onAction: (LoginEvent) -> Unit, navController: NavHostController){
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if(state.loading){
            CircularProgressIndicator()
        }
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = "logo"
        )
        LaunchedEffect(state.response) {
            if(state.response != null){
                if (state.response.nickname != null){
                    navController.navigate(route = Graph.HomeGraph) {
                        popUpTo(route = Graph.HomeGraph) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.NicknameNav)
                }
            }
        }
        Image(
            modifier = Modifier.width(600.dp).height(90.dp).padding(
                Dimens.largePadding
            ).clickable {
                kakaoLogin(context){ code ->
                    onAction(LoginEvent.KakaoLogin(KakaoAccessTokenRequest(accessToken = code)))
                    if(state.error != null){
                        Toast.makeText(context, "로그인 에러 " + state.error, Toast.LENGTH_SHORT).show()
                    }
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

