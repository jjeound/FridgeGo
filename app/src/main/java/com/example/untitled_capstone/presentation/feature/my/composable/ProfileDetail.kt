package com.example.untitled_capstone.presentation.feature.my.composable

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.MyEvent
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.io.File

@Composable
fun ProfileDetail(profile: Profile, onEvent: (MyEvent) -> Unit, navController: NavHostController){
    val context = LocalContext.current
    var image by remember { mutableStateOf(profile.imageUrl) }
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        uri.let {
                            image = uri.toString()
                            val filePath = context.getRealPathFromURI(it)
                            if (filePath != null) {
                                onEvent(MyEvent.UploadProfileImage(File(filePath)))
                            }
                            Log.d("TargetSDK", "imageUri - selected : $uri")
                        }
                    }
                }
                Activity.RESULT_CANCELED -> Unit
            }
        }
    val imageAlbumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        addCategory(Intent.CATEGORY_OPENABLE)
    }

    Column (
        modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        image?.let {
            AsyncImage(
                modifier = Modifier.size(80.dp).clip(CircleShape),
                model = it,
                contentScale = ContentScale.Crop,
                contentDescription = "profile image",
            )
            Log.d("image", it)
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.smallPadding).clickable {
                        albumLauncher.launch(imageAlbumIntent)
                    },
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