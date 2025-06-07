package com.stone.fridge.presentation.feature.my.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.stone.fridge.MainActivity
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.presentation.feature.login.LoginUiState
import com.stone.fridge.presentation.util.PermissionDialog
import com.stone.fridge.ui.theme.CustomTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileModifyScreen(
    uiState: ProfileUiState,
    popBackStack: () -> Unit,
    myName: String?,
    imageUrl: String?,
    uploadProfileImage: (File) -> Unit,
    deleteProfileImage: () -> Unit,
    modifyNickname: (String) -> Unit,
) {
    val context = LocalContext.current
    var image by remember { mutableStateOf<String?>(imageUrl) }
    var imageFile by rememberSaveable { mutableStateOf<File?>(null) }
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    image = it.toString()
                    val filePath = context.getRealPathFromURI(it)
                    imageFile = filePath?.let { path -> File(path) } ?: context.copyUriToFile(it)
                    Log.d("TargetSDK", "imageUri - selected : $uri")
                }
            }
        }
    val imageAlbumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    val galleryPermissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
        else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                galleryPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        Log.d("gallery", "gallery permission granted")
                    }
                }
            }
        )

    var error by remember { mutableStateOf(false) }
    var nickname by remember { mutableStateOf<String>(myName ?: "") }
    val focusManager = LocalFocusManager.current
    val showDialog = remember { mutableStateOf(false) }
    LaunchedEffect(uiState) {
        Log.d("uiState", "ProfileModifyScreen - $uiState")
        if(uiState == ProfileUiState.Idle){
            error = false
            popBackStack()
        }else if(uiState is ProfileUiState.Error){
            error = true
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                title = {
                    Text(
                        text = "프로필 수정",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {popBackStack()}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                actions = {
                    Text(
                        modifier = Modifier.clickable{
                            focusManager.clearFocus()
                            if (imageFile != null) {
                                uploadProfileImage(imageFile!!)
                            }
                            if (image == null && imageUrl != null) {
                                deleteProfileImage()
                            }
                            if (nickname.isNotBlank() && nickname != myName) {
                                modifyNickname(nickname)
                            }
                        },
                        text = "완료",
                        style = CustomTheme.typography.button1,
                        color = CustomTheme.colors.textPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ){ innerPadding ->
        if(uiState == LoginUiState.Loading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = CustomTheme.colors.primary,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding
                    )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
                ){
                    if(image != null){
                        Box{
                            AsyncImage(
                                model = image,
                                contentDescription = "image",
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(90.dp).clip(CircleShape),
                            )
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.TopEnd),
                                onClick = {
                                    image = null
                                    imageFile = null
                                }
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.close),
                                    contentDescription = "delete image",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd),
                                onClick = {
                                    albumLauncher.launch(imageAlbumIntent)
                                }
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.camera),
                                    contentDescription = "get image",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.size(90.dp)
                        ){
                            Icon(
                                modifier = Modifier.size(80.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.profile),
                                contentDescription = "profile image",
                                tint = CustomTheme.colors.iconDefault)
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd),
                                onClick = {
                                    when {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            galleryPermissions[0]
                                        ) == PackageManager.PERMISSION_GRANTED ->  {
                                            albumLauncher.launch(imageAlbumIntent)
                                        }
                                        shouldShowRequestPermissionRationale(
                                            context as MainActivity,
                                            galleryPermissions[0]
                                        ) -> {
                                            showDialog.value = true
                                        }
                                        else -> {
                                            requestPermissionLauncher.launch(galleryPermissions)
                                        }
                                    }
                                }
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.camera),
                                    contentDescription = "get image",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        value = nickname,
                        onValueChange = { nickname = it },
                        placeholder = {
                            Text(
                                text = "닉네임",
                                style = CustomTheme.typography.caption2,
                                color = CustomTheme.colors.textSecondary
                            )
                        },
                        trailingIcon = {
                            if (nickname.isNotBlank()) {
                                IconButton(
                                    onClick = { nickname = "" }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.delete),
                                        contentDescription = "delete",
                                    )
                                }
                            }
                        },
                        textStyle = CustomTheme.typography.body3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CustomTheme.colors.textSecondary,
                            unfocusedBorderColor = CustomTheme.colors.textSecondary,
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.onSurface,
                            unfocusedContainerColor = CustomTheme.colors.onSurface,
                            cursorColor = CustomTheme.colors.textPrimary,
                            errorCursorColor = CustomTheme.colors.error,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                            errorBorderColor = CustomTheme.colors.error,
                        ),
                        isError = error,
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        singleLine = true,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    )
                    PermissionDialog(
                        showDialog = showDialog,
                        message = "저장소 권한이 필요합니다.",
                        onDismiss = { showDialog.value = false },
                        onConfirm = {
                            showDialog.value = false
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileModifyScreenPreview() {
    ProfileModifyScreen(
        uiState = ProfileUiState.Idle,
        popBackStack = {},
        myName = "홍길동",
        imageUrl = null,
        uploadProfileImage = {},
        deleteProfileImage = {},
        modifyNickname = {}
    )
}