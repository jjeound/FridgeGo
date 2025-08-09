package com.stone.fridge.feature.my.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.kakao.sdk.user.UserApiClient
import com.stone.fridge.core.common.levelToKor
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Profile
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.my.navigation.ProfileModifyRoute

@Composable
fun ProfileDetail(
    uiState: ProfileUiState,
    profile: Profile,
    onLogout: () -> Unit,
    logout: () -> Unit,
    deleteUser: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    var image by remember { mutableStateOf(profile.imageUrl?.toUri()) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState == ProfileUiState.Logout){
            showDialog = false
            onLogout()
        } else if( uiState is ProfileUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    Column (
        modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = Dimens.mediumPadding,
                vertical = Dimens.smallPadding
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            profile.trustLevelImageUrl?.let {
                AsyncImage(
                    modifier = Modifier.size(28.dp),
                    model = it,
                    contentScale = ContentScale.Crop,
                    contentDescription = "trust level",
                )
                Spacer(
                    modifier = Modifier.width(Dimens.smallPadding)
                )
            }
            Text(
                text = profile.trustLevel?.levelToKor() ?: "초보 요리사",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
        }
        image?.let {
            AsyncImage(
                modifier = Modifier.size(80.dp).clip(CircleShape),
                model = it,
                contentScale = ContentScale.Crop,
                contentDescription = "profile image",
            )
        } ?: Icon(
            modifier = Modifier.size(80.dp),
            imageVector = ImageVector.vectorResource(R.drawable.profile),
            contentDescription = "profile image",
            tint = CustomTheme.colors.iconDefault)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = profile.nickname ?: "USER",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
            Text(
                text = profile.email,
                style = CustomTheme.typography.caption1,
                color = CustomTheme.colors.textSecondary,
            )
        }
        Spacer(
            modifier = Modifier.height(Dimens.mediumPadding)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            Text(
                text = "설정",
                style = CustomTheme.typography.title2,
                color = CustomTheme.colors.textPrimary,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {
                        logout()
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "로그아웃",
                        style = CustomTheme.typography.body1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                        contentDescription = "navigate",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = CustomTheme.colors.borderLight
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
            )  {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {
                        composeNavigator.navigate(
                            ProfileModifyRoute
                        )
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "프로필 변경",
                        style = CustomTheme.typography.body1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                        contentDescription = "navigate",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = CustomTheme.colors.borderLight
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
            )  {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {
                        showDialog = true
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "회원 탈퇴",
                        style = CustomTheme.typography.body1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                        contentDescription = "navigate",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = CustomTheme.colors.borderLight
                )
            }
        }
        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
                        horizontalArrangement = Arrangement.Center,
                    )  {
                        Text(
                            text = "회원을 탈퇴하겠습니까?",
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                },
                containerColor = CustomTheme.colors.onSurface,
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomTheme.colors.onSurface,
                            contentColor = CustomTheme.colors.textPrimary,
                        ),
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(
                            text = "취소",
                            style = CustomTheme.typography.button1,
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomTheme.colors.primary,
                            contentColor = CustomTheme.colors.onPrimary,
                        ),
                        onClick = {
                            unlink(
                                onSuccess = {
                                    deleteUser()
                                },
                                onFailure = {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    ) {
                        Text(
                            text = "탈퇴하기",
                            style = CustomTheme.typography.button1,
                        )
                    }
                }
            )
        }
    }
}

private fun unlink(onSuccess: () -> Unit, onFailure: (String) -> Unit){
    UserApiClient.instance.unlink { error ->
        if (error != null) {
            onFailure("다시 시도해주세요.")
            Log.e("ProfileDetail", "unlink failed: $error")
        } else {
            onSuccess()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileDetailPreview() {
    ProfileDetail(
        profile = Profile(
            id = 1,
            nickname = "닉네임",
            email = "이메일",
            imageUrl = null,
            trustLevelImageUrl = "",
            trustLevel = "",
        ),
        uiState = ProfileUiState.Idle,
        onLogout = {},
        logout = {},
        deleteUser = {},
        onShowSnackbar = {_, _ -> }
    )
}