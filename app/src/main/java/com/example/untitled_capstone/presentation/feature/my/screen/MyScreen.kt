package com.example.untitled_capstone.presentation.feature.my.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.fridge.composable.PermissionDialog
import com.example.untitled_capstone.presentation.feature.my.MyEvent
import com.example.untitled_capstone.presentation.feature.my.MyState
import com.example.untitled_capstone.presentation.feature.my.composable.MyAccountContainer
import com.example.untitled_capstone.presentation.feature.my.composable.MyContainer
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyScreen(navController: NavHostController, onEvent: (MyEvent) -> Unit, state: MyState, nickname: String?) {
    val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                locationPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        Log.d(permission, "위치 권한이 허용되었습니다.")
                    }
                }
            }
        )
    val context = LocalContext.current
    val packageName = context.packageName
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
    ) {
        MyAccountContainer(
            navigateTo = { navController.navigate(route = Screen.Profile(null)) },
            nickname = nickname ?: ""
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
                MyContainer("좋아요한 글", R.drawable.heart, {navController.navigate(Screen.MyLikedPostNav)})
                MyContainer("나의 게시물", R.drawable.article, {navController.navigate(Screen.MyPostNav)})
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

                MyContainer("내 동네 설정", R.drawable.location){
                    checkPermission(locationPermissions, context, showDialog){
                        if(it){
                            navController.navigate(Screen.LocationNav)
                        }else{
                            requestPermissionLauncher.launch(locationPermissions)
                        }
                    }
                }
                MyContainer("앱 설정", R.drawable.info, {})
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
                    text = "고객 지원",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                MyContainer("고객센터", R.drawable.headset, {})
                MyContainer("약관 및 정책", R.drawable.setting, {})
            }
        }
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

private fun checkPermission(locationPermissions: Array<String>, context: Context, showDialog: MutableState<Boolean>, onRequest: (Boolean) -> Unit) {
    when {
        ContextCompat.checkSelfPermission(
            context,
            locationPermissions[0]
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    locationPermissions[1]
                ) == PackageManager.PERMISSION_GRANTED->  {
            onRequest(true)
        }
        shouldShowRequestPermissionRationale(
            context as MainActivity,
            locationPermissions[0]
        ) -> {
            showDialog.value = true
        }
        else -> {
            onRequest(false)
        }
    }
}