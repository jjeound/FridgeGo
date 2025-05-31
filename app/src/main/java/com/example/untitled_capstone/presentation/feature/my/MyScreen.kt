package com.example.untitled_capstone.presentation.feature.my

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.util.PermissionDialog
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyScreen(
    profile: Profile?,
    navigate: (Screen) -> Unit,
) {
    val context = LocalContext.current
    val packageName = context.packageName
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
    ) {
        MyAccountContainer(
            navigateUp = { navigate(Screen.Profile(null)) },
            nickname = profile?.nickname,
            image = profile?.imageUrl
        )
        Card(
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                Text(
                    text = "나의 활동",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                MyContainer("좋아요한 글", R.drawable.heart) { navigate(Screen.MyLikedPostNav) }
                MyContainer("나의 게시물", R.drawable.article) { navigate(Screen.MyPostNav) }
            }
        }
        Card(
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                Text(
                    text = "설정",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )

                MyContainer("내 동네 설정", R.drawable.location, { navigate(Screen.LocationNav) })
                MyContainer("앱 설정", R.drawable.info, {})
            }
        }
//        Card(
//            shape = RoundedCornerShape(Dimens.cornerRadius),
//            colors = CardDefaults.cardColors(
//                containerColor = CustomTheme.colors.onSurface
//            )
//        ) {
//            Column(
//                modifier = Modifier.padding(Dimens.mediumPadding),
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
//            ) {
//                Text(
//                    text = "고객 지원",
//                    style = CustomTheme.typography.title2,
//                    color = CustomTheme.colors.textPrimary,
//                )
//                MyContainer("고객센터", R.drawable.headset) {}
//                MyContainer("약관 및 정책", R.drawable.setting) {}
//            }
//        }
    }
    PermissionDialog(
        showDialog = showDialog,
        message = "위치 권한이 필요합니다.",
        onDismiss = { showDialog.value = false },
        onConfirm = {
            showDialog.value = false
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
            )
        }
    )
}