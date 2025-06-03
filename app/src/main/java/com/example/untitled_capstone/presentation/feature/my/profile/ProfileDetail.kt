package com.example.untitled_capstone.presentation.feature.my.profile

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun ProfileDetail(
    uiState: ProfileUiState,
    profile: Profile,
    goToLoginScreen: () -> Unit,
    navigate: (Screen) -> Unit,
    logout: () -> Unit,
){
    var image by remember { mutableStateOf(profile.imageUrl?.toUri()) }
    if(uiState == ProfileUiState.Logout){
        goToLoginScreen()
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
                text = levelToKor(profile.trustLevel),
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
                        navigate(Screen.ProfileModifyNav(profile.nickname, profile.imageUrl))
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {  }, //TODO: 회원 탈퇴 기능 구현
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
    }
}

fun Context.getRealPathFromURI(uri: Uri): String? {
    var filePath: String? = null
    contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            filePath = cursor.getString(columnIndex)
        }
    }
    return filePath
}

fun levelToKor(level: String? = null): String{
    return when(level){
        "BeginnerChef" -> "초보 요리사"
        "HomeCook" -> "집밥러"
        "SousChef" -> "수셰프"
        "HeadChef" -> "헤드셰프"
        "MasterChef" -> "마스터 셰프"
        else -> "초보 요리사"
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
            trustLevelImageUrl = null,
            trustLevel = null,
        ),
        uiState = ProfileUiState.Idle,
        goToLoginScreen = {},
        navigate = {},
        logout = {}
    )
}