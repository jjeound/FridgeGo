package com.example.untitled_capstone.feature.refrigerator.presentation.composable

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavHostController
import androidx.work.Data
import coil.compose.AsyncImage
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFridgeItemForm(navController: NavHostController){
    var image by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val packageName = context.packageName
    val showDialog = remember { mutableStateOf(false) }

    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        uri.let {
                            image = uri
                            Log.d("TargetSDK", "imageUri - selected : $uri")
                        }
                    }
                }
                Activity.RESULT_CANCELED -> Unit
            }
        }
    val imageAlbumIntent =
        Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
        }
    val galleryPermissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
        else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionToRequest = mutableListOf<String>()
    var hasPermission: Boolean
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                hasPermission = false
                galleryPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        hasPermission = true
                    }else{
                        permissionToRequest.add(permission)
                        val rationaleRequired = shouldShowRequestPermissionRationale(
                            context as MainActivity,
                            permission
                        )
                        if(rationaleRequired){
                            Toast.makeText(context, "Permission is required for this feature", Toast.LENGTH_LONG).show()
                        } else {
                            showDialog.value = true
                        }
                    }
                }
                if (hasPermission) {
                    albumLauncher.launch(imageAlbumIntent)
                }
            }
        )
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expirationDate by remember { mutableLongStateOf(0L) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        expirationDate = it
        convertMillisToDate(it)
    } ?: ""
    val focusManager = LocalFocusManager.current
    var validator by remember { mutableStateOf(false) }
    validator = name.isNotBlank() && selectedDate.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            if(image != null){
                Box{
                    AsyncImage(
                        model = image,
                        contentDescription = "image",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    )
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = {
                            requestPermissionLauncher.launch(galleryPermissions)
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
                    modifier = Modifier
                        .size(300.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        .background(CustomTheme.colors.surface)
                ){
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = {
                            requestPermissionLauncher.launch(galleryPermissions)
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                Column {
                    Row {
                        Text(
                            text = "이름",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Text(
                            text = " *",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.iconRed,
                        )
                    }
                    TextField(
                        value = name,
                        onValueChange = {name = it},
                        modifier = Modifier.width(80.dp),
                        textStyle = CustomTheme.typography.button2,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.onSurface,
                            unfocusedContainerColor = CustomTheme.colors.onSurface,
                            cursorColor = CustomTheme.colors.textPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        singleLine = true,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(80.dp),
                        thickness = 1.dp,
                        color = CustomTheme.colors.border
                    )
                }
                Column {
                    Row {
                        Text(
                            text = "수량",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                    TextField(
                        value = quantity,
                        onValueChange = {quantity = it},
                        modifier = Modifier.width(80.dp),
                        textStyle = CustomTheme.typography.button2,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.onSurface,
                            unfocusedContainerColor = CustomTheme.colors.onSurface,
                            cursorColor = CustomTheme.colors.textPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(80.dp),
                        thickness = 1.dp,
                        color = CustomTheme.colors.border
                    )
                }
            }
            Column {
                Row {
                    Text(
                        text = "유통기한",
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Text(
                        text = " *",
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.iconRed,
                    )
                }
                TextField(
                    modifier = Modifier.width(300.dp),
                    value = selectedDate,
                    onValueChange = { },
                    textStyle = CustomTheme.typography.button2,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = !showDatePicker }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.calendar),
                                contentDescription = "Select date",
                                tint = CustomTheme.colors.iconSelected
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = CustomTheme.colors.textPrimary,
                        unfocusedTextColor = CustomTheme.colors.textPrimary,
                        focusedContainerColor = CustomTheme.colors.onSurface,
                        unfocusedContainerColor = CustomTheme.colors.onSurface,
                        cursorColor = CustomTheme.colors.textPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                        unfocusedTrailingIconColor = Color.Transparent,
                    ),

                )
                HorizontalDivider(
                    modifier = Modifier.width(300.dp),
                    thickness = 1.dp,
                    color = CustomTheme.colors.border
                )
                if(showDatePicker){
                    DatePickerDialog(
                        onDismissRequest = {},
                        confirmButton = {
                            TextButton(onClick = {
                                showDatePicker = false
                            }) {
                                Text(
                                    text = "확인",
                                    color = CustomTheme.colors.textPrimary,
                                    style = CustomTheme.typography.button2,
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {showDatePicker = false}) {
                                Text(
                                    text = "취소",
                                    color = CustomTheme.colors.textPrimary,
                                    style = CustomTheme.typography.button2,
                                )
                            }
                        },
                        colors = DatePickerDefaults.colors(
                            containerColor = CustomTheme.colors.onSurface,
                        )
                    ) {
                        DatePicker(
                            state = datePickerState,
                            showModeToggle = false,
                            title = {
                                Text(
                                    text = ""
                                )
                            },
                            colors = DatePickerDefaults.colors(
                                containerColor = CustomTheme.colors.onSurface,
                            )
                        )
                    }
                }
            }
            Button(
                onClick = {
                },
                shape = RoundedCornerShape(Dimens.cornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.primary,
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "스캔하기",
                    style = CustomTheme.typography.button1,
                    color = CustomTheme.colors.onPrimary
                )
            }
        }
        PermissionDialog(
            showDialog = showDialog,
            message = "이미지를 선택하려면 저장소 접근 권한이 필요합니다.",
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
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.mediumPadding),
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.primary,
                disabledContainerColor = CustomTheme.colors.onSurface,
                contentColor = CustomTheme.colors.onPrimary,
                disabledContentColor = CustomTheme.colors.textTertiary
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CustomTheme.colors.border
            ),
            enabled = validator,
            onClick = { navController.popBackStack() }
        ) {
            Text(
                text = "등록하기",
                style = CustomTheme.typography.button1,
            )
        }
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    return formatter.format(Date(millis))
}

