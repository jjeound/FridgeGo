package com.example.untitled_capstone.presentation.feature.my.composable

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.MyEvent
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun ProfileDetail(profile: Profile, onEvent: (MyEvent) -> Unit, navController: NavHostController){
    Column (
        modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        profile.profileImage?.let {
            AsyncImage(
                modifier = Modifier.size(80.dp),
                model = it.originalFilename.toUri(),
                contentScale = ContentScale.Fit,
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
        Text(
            modifier = Modifier.clickable{
                onEvent(MyEvent.Logout)
            },
            text = "로그아웃",
            style = CustomTheme.typography.button2,
            color = CustomTheme.colors.textPrimary,
        )
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {
                        navController.navigate(Screen.NicknameNav)
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "닉네임 변경",
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {  },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "프로필 이미지 변경",
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {  },
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
